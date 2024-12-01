package br.com.mateusfilpo.netflix.repositories;

import br.com.mateusfilpo.netflix.domain.Genre;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class GenreRepositoryTest {

    @Autowired
    private GenreRepository repository;

    private Genre newGenre;

    @BeforeEach
    void setUp() {
        newGenre = new Genre();
        newGenre.setName("New Genre");
    }

    @Test
    void shouldReturnListOfGenresWhenFindAllIsCalled() {
        List<Genre> result = repository.findAll();

        assertNotNull(result);
    }

    @Test
    void shouldReturnGenreWhenSaveIsCalled() {
        Genre savedGenre = repository.save(newGenre);

        assertNotNull(savedGenre);
        assertNotNull(savedGenre.getId());
        assertEquals(newGenre.getName(), savedGenre.getName());
    }

    @Test
    void shouldReturnGenreWhenFindByIdIsCalled() {
        Genre savedGenre = repository.save(newGenre);

        Optional<Genre> foundGenre  = repository.findById(savedGenre.getId());

        assertTrue(foundGenre.isPresent());
        assertEquals(newGenre.getName(), foundGenre.get().getName());
        assertEquals(savedGenre.getId(), foundGenre.get().getId());
    }

    @Test
    void shouldReturnEmptyOptionalWhenFindByIdIsCalledWithNonExistentId() {
        final long NON_EXISTENT_ID = 999L;

        Optional<Genre> foundGenre = repository.findById(NON_EXISTENT_ID);

        assertFalse(foundGenre.isPresent());
    }

    @Test
    void shouldDeleteGenreWhenDeleteByIdIsCalled() {
        Genre savedGenre = repository.save(newGenre);

        repository.deleteById(savedGenre.getId());

        Optional<Genre> foundGenre = repository.findById(savedGenre.getId());
        assertFalse(foundGenre.isPresent());
    }

    @Test
    void shouldReturnTrueWhenGenreExistsByName() {
        repository.save(newGenre);

        boolean exists = repository.existsByName(newGenre.getName());

        assertTrue(exists);
    }

    @Test
    void shouldReturnFalseWhenGenreDoesNotExistByName() {
        final String NON_EXISTENT_NAME = "NON_EXISTENT_NAME";

        boolean exists = repository.existsByName(NON_EXISTENT_NAME);

        assertFalse(exists);
    }
}