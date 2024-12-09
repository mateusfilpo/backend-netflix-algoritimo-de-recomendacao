package br.com.mateusfilpo.netflix.services;

import br.com.mateusfilpo.netflix.domain.*;
import br.com.mateusfilpo.netflix.dtos.movies.MovieWithValueGenreDTO;
import br.com.mateusfilpo.netflix.dtos.usergenres.UserGenreDTO;
import br.com.mateusfilpo.netflix.dtos.users.UserCreateDTO;
import br.com.mateusfilpo.netflix.dtos.users.UserResponseDTO;
import br.com.mateusfilpo.netflix.dtos.users.UserUpdateDTO;
import br.com.mateusfilpo.netflix.repositories.GenreRepository;
import br.com.mateusfilpo.netflix.repositories.MovieRepository;
import br.com.mateusfilpo.netflix.repositories.RoleRepository;
import br.com.mateusfilpo.netflix.repositories.UserRepository;
import br.com.mateusfilpo.netflix.services.exceptions.UserNotFoundException;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(SpringExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserService service;

    @Mock
    private UserRepository userRepository;

    @Mock
    private GenreRepository genreRepository;

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private CacheManager cacheManager;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private Cache userListMovieRecommendCache;

    private List<User> userList;
    private Long existingId;
    private Long nonExistingId;
    private User user;
    private Set<Long> genreIds;
    private int pageNumber;
    private int pageSize;
    private PageRequest pageRequest;
    private Page<Movie> moviePage;
    private List<Movie> movies;
    private Movie movie;
    private Movie movie2;
    private MovieGenre movieGenre;
    private MovieGenre movieGenre2;
    private Genre genre;
    private UserGenre userGenre;
    private UserCreateDTO userCreateDTO;
    private UserGenreDTO userGenreDTO;
    private Role role;
    private Long genreId;
    private UserUpdateDTO userUpdateDTO;

    @BeforeEach
    void setUp() {
        userList = new ArrayList<>();
        existingId = 1L;
        nonExistingId = 999L;
        user = new User();
        user.setId(1L);
        user.setUsername("Username");
        user.setFirstName("First Name");
        genreIds = new HashSet<>();
        genreIds.add(1L);
        pageNumber = 0;
        pageSize = 20;
        pageRequest = PageRequest.of(pageNumber, pageSize, Sort.by("title"));
        movies = new ArrayList<>();
        genre = new Genre();
        genre.setId(1L);
        genre.setName("Ação");
        movie = new Movie();
        movie.setId(1L);
        movie.setTitle("Novo Filme");
        movieGenre = new MovieGenre(null, movie, genre, 1.0);
        movie.getGenres().add(movieGenre);
        genreId = 1L;

        movie2 = new Movie();
        movie2.setId(2L);
        movie2.setTitle("Novo Filme 2");
        movieGenre2 = new MovieGenre(null, movie2, genre, 0.0);
        movie2.getGenres().add(movieGenre2);

        movies.add(movie);
        movies.add(movie2);

        userGenre = new UserGenre(null, user, genre);
        userGenre.setValue(1.0);
        user.getGenres().add(userGenre);
        moviePage = new PageImpl<>(movies, pageRequest, movies.size());

        userCreateDTO = new UserCreateDTO();
        userCreateDTO.setFirstName("First Name");
        userCreateDTO.setUsername("username");
        userCreateDTO.setLastName("Last Name");
        userCreateDTO.setEmail("email@email.com");
        userCreateDTO.setPassword("123456");
        userGenreDTO = new UserGenreDTO();
        userGenreDTO.setId(1L);
        userCreateDTO.getGenres().add(userGenreDTO);

        role = new Role();
        role.setAuthority("ROLE_USER");

        userUpdateDTO = new UserUpdateDTO();
        userUpdateDTO.setFirstName("Att first name");
        userUpdateDTO.setLastName("Att last name");
        userUpdateDTO.setUsername("att username");
        userUpdateDTO.setPassword("123456");
        userUpdateDTO.setEmail("attemail@email.com");


        Mockito.when(userRepository.findAll()).thenReturn(userList);
        Mockito.when(userRepository.findById(existingId)).thenReturn(Optional.of(user));
        Mockito.when(userRepository.findById(nonExistingId)).thenThrow(UserNotFoundException.class);
        Mockito.when(movieRepository.findMoviesByGenreIds(genreIds, pageRequest)).thenReturn(moviePage);
        // Mockito.when(movieRepository.findMoviesByGenreIds(genreIds, pageRequest)).thenReturn(moviePage);
        Mockito.when(cacheManager.getCache("userListMovieRecommendCache")).thenReturn(userListMovieRecommendCache);
        Mockito.when(genreRepository.findById(genreId)).thenReturn(Optional.of(genre));
        Mockito.when(passwordEncoder.encode(userCreateDTO.getPassword())).thenReturn("encodedPassword");
        Mockito.when(roleRepository.findByAuthority("ROLE_USER")).thenReturn(role);
        Mockito.when(userRepository.save(any(User.class))).thenReturn(user);
        Mockito.when(userRepository.existsById(existingId)).thenReturn(true);
        Mockito.when(userRepository.existsById(nonExistingId)).thenReturn(false);
        Mockito.when(userRepository.getReferenceById(existingId)).thenReturn(user);
        Mockito.doNothing().when(userRepository).deleteById(existingId);
    }

    @Test
    void findAllUsersShouldReturnListOfUserResponseDTOs() {
        List<UserResponseDTO> result = service.findAll();

        assertNotNull(result);

        Mockito.verify(userRepository, Mockito.times(1)).findAll();
    }

    @Test
    void findByIdShouldReturnUserResponseDTOWhenIdExists() {
        UserResponseDTO result = service.findById(existingId);

        assertNotNull(result);
        assertEquals(user.getFirstName(), result.getFirstName());

        Mockito.verify(userRepository, Mockito.times(1)).findById(existingId);
    }

    @Test
    void findByIdShouldThrowUserNotFoundExceptionWhenIdDoesNotExist() {
        assertThrows(UserNotFoundException.class, () -> service.findById(nonExistingId));

        Mockito.verify(userRepository, Mockito.times(1)).findById(nonExistingId);
    }

    @Test
    void findRecommendedMoviesShouldReturnPageOfMovieWithValueGenreDTOWhenUserExists() {
        Page<MovieWithValueGenreDTO> result = service.findRecommendedMovies(existingId, pageNumber, pageSize);

        assertNotNull(result);
        assertEquals(pageNumber, result.getNumber());
        assertEquals(pageSize, result.getSize());
        assertEquals(movie.getTitle(), result.getContent().get(0).getTitle());
        assertEquals(movie2.getTitle(), result.getContent().get(1).getTitle());
        assertEquals(2, result.getTotalElements());
    }

    @Test
    void findRecommendedMoviesShouldThrowUserNotFoundExceptionWhenUserDoesNotExist() {
        assertThrows(UserNotFoundException.class, () -> service.findRecommendedMovies(nonExistingId, pageNumber, pageSize));

        Mockito.verify(userRepository, Mockito.times(1)).findById(nonExistingId);
    }

    @Test
    void createUserShouldReturnUserIdWhenUserIsSuccessfullyCreated() {
        Long result = service.createUser(userCreateDTO);

        assertEquals(user.getId(), result);

        Mockito.verify(userListMovieRecommendCache, Mockito.times(1)).clear();
        Mockito.verify(passwordEncoder, Mockito.times(1)).encode(userCreateDTO.getPassword());
        Mockito.verify(genreRepository, Mockito.times(1)).findById(genreId);
        Mockito.verify(userRepository, Mockito.times(1)).save(any(User.class));
    }

    @Test
    void updateUserShouldUpdateUserWhenIdExists() {
        service.updateUser(existingId, userUpdateDTO);

        Mockito.verify(userRepository, Mockito.times(1)).existsById(existingId);
        Mockito.verify(userRepository).getReferenceById(existingId);
        Mockito.verify(userRepository).save(any(User.class));
        Mockito.verify(userListMovieRecommendCache, Mockito.times(1)).clear();
    }

    @Test
    void updateUserShouldThrowExceptionWhenIdDoesNotExist() {
        assertThrows(UserNotFoundException.class, () -> service.updateUser(nonExistingId, userUpdateDTO));

        Mockito.verify(userRepository, Mockito.times(1)).existsById(nonExistingId);
        Mockito.verify(userRepository, Mockito.never()).getReferenceById(nonExistingId);
        Mockito.verify(userRepository, Mockito.never()).save(any(User.class));
    }

    @Test
    void deleteUserShouldDeleteUserWhenIdExists() {
        service.deleteUser(existingId);

        Mockito.verify(userRepository, Mockito.times(1)).existsById(existingId);
        Mockito.verify(userRepository, Mockito.times(1)).deleteById(existingId);
        Mockito.verify(userListMovieRecommendCache, Mockito.times(1)).clear();

    }

    @Test
    void deleteUserShouldThrowUserNotFoundExceptionWhenIdDoesNotExist() {
        assertThrows(UserNotFoundException.class, () -> service.deleteUser(nonExistingId));

        Mockito.verify(userRepository, Mockito.times(1)).existsById(nonExistingId);
        Mockito.verify(userRepository, Mockito.never()).deleteById(nonExistingId);
    }

}