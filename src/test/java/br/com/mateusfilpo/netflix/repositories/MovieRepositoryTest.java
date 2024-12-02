package br.com.mateusfilpo.netflix.repositories;

import br.com.mateusfilpo.netflix.domain.Genre;
import br.com.mateusfilpo.netflix.domain.Movie;
import br.com.mateusfilpo.netflix.domain.MovieGenre;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class MovieRepositoryTest {

    @Autowired
    private MovieRepository repository;

    @PersistenceContext
    private EntityManager entityManager;

    private Movie newMovie;
    @Autowired
    private MovieRepository movieRepository;

    @BeforeEach
    void setUp() {
        newMovie = new Movie();
        newMovie.setTitle("New Movie Title");
        newMovie.setDescription("New movie description");
        newMovie.getGenres().add(new MovieGenre(null, newMovie, new Genre(1L, "Ação"), 1.0));
    }

    @Test
    void shouldReturnListOfMoviesWhenFindAllIsCalled() {
        List<Movie> result = repository.findAll();

        assertNotNull(result);
    }

    @Test
    void shouldReturnMovieWhenFindByIdIsCalled() {
        Movie savedMovie = repository.save(newMovie);

        Optional<Movie> foundMovie  = repository.findById(savedMovie.getId());

        assertTrue(foundMovie.isPresent());
        assertEquals(savedMovie.getTitle(), foundMovie.get().getTitle());
        assertEquals(savedMovie.getDescription(), foundMovie.get().getDescription());
        assertEquals(savedMovie.getId(), foundMovie.get().getId());
    }

    @Test
    void shouldReturnEmptyOptionalWhenFindByIdIsCalledWithNonExistentId() {
        final long NON_EXISTENT_ID = 999L;

        Optional<Movie> foundMovie = repository.findById(NON_EXISTENT_ID);

        assertFalse(foundMovie.isPresent());
    }

    @Test
    void shouldReturnMovieWhenSaveIsCalled() {
        Movie savedMovie = repository.save(newMovie);

        assertNotNull(savedMovie);
        assertNotNull(savedMovie.getId());
        assertEquals(newMovie.getTitle(), savedMovie.getTitle());
        assertEquals(newMovie.getDescription(), savedMovie.getDescription());
    }

    @Test
    void shouldDeleteMovieWhenDeleteByIdIsCalled() {
        Movie savedMovie = repository.save(newMovie);

        repository.deleteById(savedMovie.getId());

        Optional<Movie> foundMovie = repository.findById(savedMovie.getId());
        assertFalse(foundMovie.isPresent());
    }

    @Test
    void shouldReturnMoviesWhenFindMoviesByGenreIdsIsCalled() {
        PageRequest pageRequest = PageRequest.of(0, 20, Sort.by(Sort.Order.asc("id")));
        Set<Long> genresId = new HashSet<>();
        genresId.add(1L);
        Page<Movie> result = repository.findMoviesByGenreIds(genresId, pageRequest);
        result.getContent().forEach(x -> System.out.println(x.getTitle()));

        assertNotNull(result);
        assertEquals(19, result.getTotalElements());
        assertEquals(newMovie.getGenres().getFirst().getGenre().getName(), result.getContent().get(0).getGenres().getFirst().getGenre().getName());
    }

    @Test
    void shouldRemoveGenresFromMovieWhenDeleteMovieGenresIsCalled() {
        Movie savedMovie = repository.save(newMovie);

        repository.deleteMovieGenres(savedMovie.getId());
        entityManager.flush();
        entityManager.clear();

        Movie foundMovie = repository.findById(savedMovie.getId()).get();

        assertEquals(0, foundMovie.getGenres().size());
    }

    @Test
    void shouldReturnMoviesByGenre() {
        PageRequest pageRequest = PageRequest.of(0, 20, Sort.by(Sort.Order.asc("id")));

        Page<Movie> moviePage = movieRepository.findAllMoviesByGenre(1L, pageRequest);

        assertNotNull(moviePage);
        assertEquals(19, moviePage.getTotalElements());
    }
}