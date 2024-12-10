package br.com.mateusfilpo.netflix.services;

import br.com.mateusfilpo.netflix.domain.Genre;
import br.com.mateusfilpo.netflix.domain.Movie;
import br.com.mateusfilpo.netflix.domain.MovieGenre;
import br.com.mateusfilpo.netflix.dtos.movies.MovieCreateDTO;
import br.com.mateusfilpo.netflix.dtos.moviegenres.MovieGenreDTO;
import br.com.mateusfilpo.netflix.dtos.movies.MovieKafkaDTO;
import br.com.mateusfilpo.netflix.dtos.movies.MovieResponseDTO;
import br.com.mateusfilpo.netflix.dtos.movies.MovieUpdateDTO;
import br.com.mateusfilpo.netflix.repositories.GenreRepository;
import br.com.mateusfilpo.netflix.repositories.MovieRepository;
import br.com.mateusfilpo.netflix.services.exceptions.GenreNotFoundException;
import br.com.mateusfilpo.netflix.services.exceptions.MovieNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

import static br.com.mateusfilpo.netflix.utils.PaginationUtils.buildPageRequest;

@Service
public class MovieService {

    private final MovieRepository repository;
    private final GenreRepository genreRepository;
    private final CacheManager cacheManager;
    private final KafkaTemplate<String, Serializable> kafkaTemplate;

    public MovieService(MovieRepository repository, GenreRepository genreRepository, CacheManager cacheManager, KafkaTemplate<String, Serializable> kafkaTemplate) {
        this.repository = repository;
        this.genreRepository = genreRepository;
        this.cacheManager = cacheManager;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Cacheable(cacheNames = "movieListCache")
    public Page<MovieResponseDTO> findAllMovies(Integer pageNumber, Integer pageSize) {
        PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, "title");

        Page<Movie> moviePage;

        moviePage = repository.findAll(pageRequest);

        return moviePage.map(MovieResponseDTO::new);
    }

    @Cacheable(cacheNames = "movieCache", key = "#id")
    public MovieResponseDTO findById(Long id) {
        return new MovieResponseDTO(repository.findById(id).orElseThrow(() -> new MovieNotFoundException(id)));
    }

    @Cacheable(cacheNames = "movieListByGenreCache")
    public Page<MovieResponseDTO> findAllMoviesByGenre(Long genreId, Integer pageNumber, Integer pageSize) {
        PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, "title");

        Page<Movie> moviePage = repository.findAllMoviesByGenre(genreId, pageRequest);

        return moviePage.map(MovieResponseDTO::new);
    }

    public Long createMovie(MovieCreateDTO movieCreateDTO) {
        if (cacheManager.getCache("movieListCache") != null) {
            cacheManager.getCache("movieListCache").clear();
        }
        if (cacheManager.getCache("movieListByGenreCache") != null) {
            cacheManager.getCache("movieListByGenreCache").clear();
        }

        Movie movie = new Movie(movieCreateDTO);

        processGenres(movie, movieCreateDTO.getGenres());

        Long id = repository.save(movie).getId();

        MovieKafkaDTO kafkaDTO = new MovieKafkaDTO(movie.getTitle(), movie.getDescription());

        kafkaTemplate.send("new-movie-notification", kafkaDTO);

        return id;
    }

    @Transactional
    public void updateMovie(Long id, MovieUpdateDTO movieUpdateDTO) {
        clearCache(id);

        if (!repository.existsById(id)) {
            throw new MovieNotFoundException(id);
        }

        Movie movie = repository.getReferenceById(id);
        updateData(movie, movieUpdateDTO);
        repository.save(movie);
    }

    public void deleteMovie(Long id) {
        clearCache(id);

        if (!repository.existsById(id)) {
            throw new MovieNotFoundException(id);
        }

        repository.deleteById(id);
    }

    private void clearCache(Long id) {
        if (cacheManager.getCache("movieCache") != null) {
            cacheManager.getCache("movieCache").evict(id);
        }
        if (cacheManager.getCache("movieListCache") != null) {
            cacheManager.getCache("movieListCache").clear();
        }
        if (cacheManager.getCache("movieListByGenreCache") != null) {
            cacheManager.getCache("movieListByGenreCache").clear();
        }
    }

    private void updateData(Movie movie, MovieUpdateDTO dto) {
        if (dto.getTitle() != null) {
            movie.setTitle(dto.getTitle());
        }

        if (dto.getDescription() != null) {
            movie.setDescription(dto.getDescription());
        }

        if (dto.getGenres() != null && !dto.getGenres().isEmpty()) {
            repository.deleteMovieGenres(movie.getId());
            processGenres(movie, dto.getGenres());
        }
    }

    private void processGenres(Movie movie, List<MovieGenreDTO> genresDTO) {
        genresDTO.forEach(genreDTO -> {
            Genre genre = genreRepository.findById(genreDTO.getId())
                    .orElseThrow(() -> new GenreNotFoundException(genreDTO.getId()));

            MovieGenre movieGenre = new MovieGenre();
            movieGenre.setGenre(genre);
            movieGenre.setMovie(movie);
            movieGenre.setValue(genreDTO.getValue());

            movie.getGenres().add(movieGenre);
        });
    }
}
