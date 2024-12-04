package br.com.mateusfilpo.netflix.services;

import br.com.mateusfilpo.netflix.domain.Genre;
import br.com.mateusfilpo.netflix.domain.Movie;
import br.com.mateusfilpo.netflix.dtos.moviegenres.MovieGenreDTO;
import br.com.mateusfilpo.netflix.dtos.movies.MovieCreateDTO;
import br.com.mateusfilpo.netflix.dtos.movies.MovieResponseDTO;
import br.com.mateusfilpo.netflix.dtos.movies.MovieUpdateDTO;
import br.com.mateusfilpo.netflix.repositories.GenreRepository;
import br.com.mateusfilpo.netflix.repositories.MovieRepository;
import br.com.mateusfilpo.netflix.services.exceptions.MovieNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(SpringExtension.class)
public class MovieServiceTest {

    @InjectMocks
    private MovieService service;

    @Mock
    private MovieRepository repository;

    @Mock
    private GenreRepository genreRepository;

    @Mock
    private CacheManager cacheManager;

    @Mock
    private Cache movieCache;

    @Mock
    private Cache movieListCache;

    @Mock
    private Cache movieListByGenreCache;

    private int pageSize;
    private int pageNumber;
    private PageRequest pageRequest;
    private Page<Movie> moviePage;
    private List<Movie> movies;
    private long existingId;
    private long nonExistingId;
    private long genreId;
    private Movie movie;
    private MovieCreateDTO movieCreateDTO;
    private MovieUpdateDTO movieUpdateDTO;
    private Genre genre;

    @BeforeEach
    void setUp() {
        pageNumber = 0;
        pageSize = 20;
        pageRequest = PageRequest.of(pageNumber, pageSize, Sort.by("title"));
        movies = new ArrayList<>();
        moviePage = new PageImpl<>(movies, pageRequest, movies.size());
        movie = new Movie();
        movie.setId(1L);
        movie.setTitle("New Movie");
        movie.setDescription("New Movie Description");
        movieCreateDTO = new MovieCreateDTO();
        movieCreateDTO.getGenres().add(new MovieGenreDTO(2L));
        movieUpdateDTO = new MovieUpdateDTO();
        movieUpdateDTO.getGenres().add(new MovieGenreDTO(2L));
        existingId = 1L;
        nonExistingId = 999L;
        genreId = 2L;
        genre = new Genre();

        Mockito.when(repository.findAll(pageRequest)).thenReturn(moviePage);
        Mockito.when(repository.findById(existingId)).thenReturn(Optional.of(movie));
        Mockito.when(repository.findById(nonExistingId)).thenThrow(MovieNotFoundException.class);
        Mockito.when(repository.findAllMoviesByGenre(genreId, pageRequest)).thenReturn(moviePage);
        Mockito.when(cacheManager.getCache("movieListCache")).thenReturn(movieListCache);
        Mockito.when(cacheManager.getCache("movieListByGenreCache")).thenReturn(movieListByGenreCache);
        Mockito.when(genreRepository.findById(genreId)).thenReturn(Optional.of(genre));
        Mockito.when(repository.save(any(Movie.class))).thenReturn(movie);
        Mockito.when(cacheManager.getCache("movieCache")).thenReturn(movieCache);
        Mockito.when(repository.existsById(existingId)).thenReturn(true);
        Mockito.when(repository.existsById(nonExistingId)).thenReturn(false);
        Mockito.when(repository.getReferenceById(existingId)).thenReturn(movie);
        Mockito.doNothing().when(repository).deleteById(existingId);
    }

    @Test
    void findAllMoviesShouldReturnListOfMovieResponseDTOs() {
        Page<MovieResponseDTO> result = service.findAllMovies(pageNumber, pageSize);

        assertNotNull(result);
        assertEquals(pageNumber, result.getNumber());
        assertEquals(pageSize, result.getSize());
    }

    @Test
    void findByIdShouldReturnMovieResponseDTOWhenIdExists() {
        MovieResponseDTO result = service.findById(existingId);

        assertNotNull(result);
        assertEquals(movie.getTitle(), result.getTitle());
        assertEquals(movie.getDescription(), result.getDescription());
    }

    @Test
    void findByIdShouldThrowMovieNotFoundExceptionWhenIdDoesNotExist() {
        assertThrows(MovieNotFoundException.class, () -> service.findById(nonExistingId));

        Mockito.verify(repository, Mockito.times(1)).findById(nonExistingId);
    }

    @Test
    void findAllMoviesByGenreShouldReturnPageOfMovieResponseDTOsWhenMoviesExist() {
        Page<MovieResponseDTO> result = service.findAllMoviesByGenre(genreId, pageNumber, pageSize);

        assertNotNull(result);

        Mockito.verify(repository, Mockito.times(1)).findAllMoviesByGenre(genreId, pageRequest);
    }

    @Test
    void createMovieShouldSaveMovieAndClearCaches() {
        Long result = service.createMovie(movieCreateDTO);

        assertNotNull(result);
        assertEquals(movie.getId(), result);

        Mockito.verify(movieListCache, Mockito.times(1)).clear();
        Mockito.verify(movieListByGenreCache, Mockito.times(1)).clear();
        Mockito.verify(genreRepository, Mockito.times(1)).findById(genreId);
        Mockito.verify(repository, Mockito.times(1)).save(any(Movie.class));
    }

    @Test
    void updateMovieShouldUpdateMovieSuccessfully() {
        service.updateMovie(existingId, movieUpdateDTO);

        Mockito.verify(movieListCache, Mockito.times(1)).clear();
        Mockito.verify(movieListByGenreCache, Mockito.times(1)).clear();
        Mockito.verify(movieCache, Mockito.times(1)).evict(existingId);
        Mockito.verify(repository, Mockito.times(1)).existsById(existingId);
        Mockito.verify(repository, Mockito.times(1)).getReferenceById(existingId);
        Mockito.verify(genreRepository, Mockito.times(1)).findById(genreId);
        Mockito.verify(repository, Mockito.times(1)).save(any(Movie.class));
    }

    @Test
    void updateMovieShouldThrowExceptionWhenMovieDoesNotExist() {
        assertThrows(MovieNotFoundException.class, () -> service.updateMovie(nonExistingId, movieUpdateDTO));

        Mockito.verify(repository, Mockito.times(1)).existsById(nonExistingId);
        Mockito.verify(repository, Mockito.never()).getReferenceById(nonExistingId);
        Mockito.verify(repository, Mockito.never()).save(any(Movie.class));
    }

    @Test
    void deleteMovieShouldDeleteMovieSuccessfully() {
        service.deleteMovie(existingId);

        Mockito.verify(movieListCache, Mockito.times(1)).clear();
        Mockito.verify(movieListByGenreCache, Mockito.times(1)).clear();
        Mockito.verify(movieCache, Mockito.times(1)).evict(existingId);
        Mockito.verify(repository, Mockito.times(1)).existsById(existingId);
        Mockito.verify(repository, Mockito.times(1)).deleteById(existingId);
    }

    @Test
    void deleteMovieShouldThrowExceptionWhenMovieDoesNotExist() {
        assertThrows(MovieNotFoundException.class, () -> service.deleteMovie(nonExistingId));

        Mockito.verify(repository, Mockito.times(1)).existsById(nonExistingId);
        Mockito.verify(repository, Mockito.never()).deleteById(nonExistingId);
    }
}