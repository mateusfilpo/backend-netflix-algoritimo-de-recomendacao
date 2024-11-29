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
import br.com.mateusfilpo.netflix.services.exceptions.GenreNotFoundException;
import br.com.mateusfilpo.netflix.services.exceptions.UserNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static br.com.mateusfilpo.netflix.utils.PaginationUtils.buildPageRequest;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository repository;
    private final GenreRepository genreRepository;
    private final RoleRepository roleRepository;
    private final MovieRepository movieRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository repository, GenreRepository genreRepository, RoleRepository roleRepository, MovieRepository movieRepository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.genreRepository = genreRepository;
        this.roleRepository = roleRepository;
        this.movieRepository = movieRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<UserResponseDTO> findAll() {
        return repository.findAll().stream()
                .map(UserResponseDTO::new)
                .toList();
    }

    public UserResponseDTO findById(Long id) {
        return new UserResponseDTO(repository.findById(id).orElseThrow(() -> new UserNotFoundException(id)));
    }

    public Page<MovieWithValueGenreDTO> findRecommendedMovies(Long id, Integer pageNumber, Integer pageSize) {
        User user = repository.findById(id).orElseThrow(() -> new UserNotFoundException(id));

        Map<Long, Double> userGenresMap = user.getGenres().stream()
                .collect(Collectors.toMap(userGenre -> userGenre.getGenre().getId(), userGenre -> userGenre.getValue()));

        PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, "title");

        Page<Movie> moviePage;

        moviePage = movieRepository.findMoviesByGenreIds(userGenresMap.keySet(), pageRequest);

        List<MovieWithValueGenreDTO> recommendedMovies  = moviePage.getContent().stream()
                .map(movie -> new MovieWithValueGenreDTO(movie, calculateDistance(user, movie)))
                .sorted(Comparator.comparingDouble(MovieWithValueGenreDTO::getDistance))
                .toList();

        return new PageImpl<>(recommendedMovies, pageRequest, moviePage.getTotalElements());
    }

    public Long createUser(UserCreateDTO dto) {
        User user = new User(dto);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        Role role = roleRepository.findByAuthority("ROLE_USER");
        user.getRoles().add(role);

        processGenres(user, dto.getGenres());

        return repository.save(user).getId();
    }

    @Transactional
    public void updateUser(Long id, UserUpdateDTO dto) {
        if (!repository.existsById(id)) {
            throw new UserNotFoundException(id);
        }

        User user = repository.getReferenceById(id);
        updateData(user, dto);

        repository.save(user);
    }

    public void deleteUser(Long id) {
        if (!repository.existsById(id)) {
            throw new UserNotFoundException(id);
        }

        repository.deleteById(id);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return user;
    }

    private void updateData(User user, UserUpdateDTO dto) {
        if (dto.getUsername() != null) {
            user.setUsername(dto.getUsername());
        }

        if (dto.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        if (dto.getEmail() != null) {
            user.setEmail(dto.getEmail());
        }

        if (dto.getFirstName() != null) {
            user.setFirstName(dto.getFirstName());
        }

        if (dto.getLastName() != null) {
            user.setLastName(dto.getLastName());
        }

        if (!dto.getGenres().isEmpty()) {
            repository.deleteUserGenres(user.getId());
            processGenres(user, dto.getGenres());
        }
    }

    private void processGenres(User user, List<UserGenreDTO> genresDTO) {
        genresDTO.forEach(genreDTO -> {
            Genre genre = genreRepository.findById(genreDTO.getId())
                    .orElseThrow(() -> new GenreNotFoundException(genreDTO.getId()));

            UserGenre userGenre = new UserGenre();
            userGenre.setGenre(genre);
            userGenre.setUser(user);
            userGenre.setValue(1.0);

            user.getGenres().add(userGenre);
        });
    }

    private Double calculateDistance(User user, Movie movie) {
        double distance = 0.0;

        for (UserGenre userGenre : user.getGenres()) {
            Long genreId = userGenre.getGenre().getId();
            Double userValue = userGenre.getValue();

            Optional<MovieGenre> movieGenre = movie.getGenres().stream()
                    .filter(mg -> mg.getGenre().getId().equals(genreId))
                    .findFirst();

            Double movieValue = movieGenre.map(MovieGenre::getValue).orElse(0.0);

            distance += Math.pow(userValue - movieValue, 2);
        }

        return Math.sqrt(distance);
    }

}
