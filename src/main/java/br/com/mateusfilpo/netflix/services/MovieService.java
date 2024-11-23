package br.com.mateusfilpo.netflix.services;

import br.com.mateusfilpo.netflix.domain.Genre;
import br.com.mateusfilpo.netflix.domain.Movie;
import br.com.mateusfilpo.netflix.domain.MovieGenre;
import br.com.mateusfilpo.netflix.dtos.MovieCreateDTO;
import br.com.mateusfilpo.netflix.dtos.MovieGenreDTO;
import br.com.mateusfilpo.netflix.dtos.MovieResponseDTO;
import br.com.mateusfilpo.netflix.dtos.MovieUpdateDTO;
import br.com.mateusfilpo.netflix.repositories.GenreRepository;
import br.com.mateusfilpo.netflix.repositories.MovieRepository;
import br.com.mateusfilpo.netflix.services.exceptions.GenreNotFoundException;
import br.com.mateusfilpo.netflix.services.exceptions.InsufficientGenresException;
import br.com.mateusfilpo.netflix.services.exceptions.MovieNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieService {

    private final MovieRepository repository;
    private final GenreRepository genreRepository;

    public MovieService(MovieRepository repository, GenreRepository genreRepository) {
        this.repository = repository;
        this.genreRepository = genreRepository;
    }

    public List<MovieResponseDTO> findAllMovies() {
        List<Movie> movies = repository.findAll();

        return movies.stream().map(MovieResponseDTO::new).toList();
    }

    public MovieResponseDTO findById(Long id) {
        return new MovieResponseDTO(repository.findById(id).orElseThrow(() -> new MovieNotFoundException(id)));
    }

    public Long createMovie(MovieCreateDTO movieCreateDTO) {
        Movie movie = new Movie(movieCreateDTO);

        processGenres(movie, movieCreateDTO.getGenres());

        return repository.save(movie).getId();
    }

    @Transactional
    public void updateMovie(Long id, MovieUpdateDTO movieUpdateDTO) {
        if (!repository.existsById(id)) {
            throw new MovieNotFoundException(id);
        }

        Movie movie = repository.getReferenceById(id);
        updateData(movie, movieUpdateDTO);
        repository.save(movie);
    }

    public void deleteMovie(Long id) {
        if (!repository.existsById(id)) {
            throw new MovieNotFoundException(id);
        }

        repository.deleteById(id);
    }


    private void updateData(Movie movie, MovieUpdateDTO dto) {
        if (dto.getTitle() != null) {
            movie.setTitle(dto.getTitle());
        }

        if (dto.getDescription() != null) {
            movie.setDescription(dto.getDescription());
        }

        if (dto.getGenres() != null && !dto.getGenres().isEmpty()) {
            if (dto.getGenres().size() < 3) {
                throw new InsufficientGenresException();
            }
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
