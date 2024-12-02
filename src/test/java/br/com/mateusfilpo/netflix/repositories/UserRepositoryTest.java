package br.com.mateusfilpo.netflix.repositories;

import br.com.mateusfilpo.netflix.domain.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository repository;

    @PersistenceContext
    private EntityManager entityManager;

    private User newUser;

    @BeforeEach
    void setUp() {
        newUser = new User();
        newUser.setUsername("newusername");
        newUser.setPassword("newpassword");
        newUser.setEmail("newemail@email.com");
        newUser.setFirstName("New First Name");
        newUser.setLastName("New Last Name");
        newUser.getGenres().add(new UserGenre(null, newUser, new Genre(1L, "Ação")));
        newUser.getRoles().add(new Role(1L, null));
    }

    @Test
    void shouldReturnListOfUsersWhenFindAllIsCalled() {
        List<User> result = repository.findAll();

        assertNotNull(result);
    }

    @Test
    void shouldReturnUserWhenFindByIdIsCalled() {
        User savedUser = repository.save(newUser);

        Optional<User> foundUser  = repository.findById(savedUser.getId());

        assertTrue(foundUser.isPresent());
        assertEquals(newUser.getUsername(), foundUser.get().getUsername());
        assertEquals(newUser.getFirstName(), foundUser.get().getFirstName());
        assertEquals(newUser.getEmail(), foundUser.get().getEmail());
    }

    @Test
    void shouldReturnEmptyOptionalWhenFindByIdIsCalledWithNonExistentId() {
        final long NON_EXISTENT_ID = 999L;

        Optional<User> foundUser = repository.findById(NON_EXISTENT_ID);

        assertFalse(foundUser.isPresent());
    }

    @Test
    void shouldReturnUserWhenSaveIsCalled() {
        User savedUser = repository.save(newUser);

        assertNotNull(savedUser);
        assertNotNull(savedUser.getId());
        assertEquals(newUser.getUsername(), savedUser.getUsername());
        assertEquals(newUser.getEmail(), savedUser.getEmail());
    }

    @Test
    void shouldDeleteMovieWhenDeleteByIdIsCalled() {
        User savedUser = repository.save(newUser);

        repository.deleteById(savedUser.getId());

        Optional<User> foundUser = repository.findById(savedUser.getId());
        assertFalse(foundUser.isPresent());
    }

    @Test
    void shouldRemoveGenresFromUserWhenDeleteUserGenresIsCalled() {
        User savedUser = repository.save(newUser);

        repository.deleteUserGenres(savedUser.getId());
        entityManager.flush();
        entityManager.clear();

        User foundUser = repository.findById(savedUser.getId()).get();

        assertEquals(0, foundUser.getGenres().size());
    }

    @Test
    void shouldReturnListOfUserGenresWhenFindUserGenresByUserIdIsCalled() {
        User savedUser = repository.save(newUser);

        List<UserGenre> result = repository.findUserGenresByUserId(savedUser.getId());

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void shouldReturnUserWhenFindByUsernameIsCalledWithExistingUsername() {
        User savedUser = repository.save(newUser);

        User result = repository.findByUsername(savedUser.getUsername());

        assertNotNull(result);
        assertEquals(savedUser.getUsername(), result.getUsername());
    }

    @Test
    void shouldReturnTrueWhenUsernameExists() {
        User savedUser = repository.save(newUser);

        boolean result = repository.existsByUsername(savedUser.getUsername());

        assertTrue(result);
    }

    @Test
    void shouldReturnTrueWhenEmailExists() {
        User savedUser = repository.save(newUser);

        boolean result = repository.existsByEmail(savedUser.getEmail());

        assertTrue(result);
    }

}