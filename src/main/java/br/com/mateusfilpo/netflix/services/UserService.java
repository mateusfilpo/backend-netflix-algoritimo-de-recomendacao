package br.com.mateusfilpo.netflix.services;

import br.com.mateusfilpo.netflix.domain.*;
import br.com.mateusfilpo.netflix.dtos.*;
import br.com.mateusfilpo.netflix.repositories.GenreRepository;
import br.com.mateusfilpo.netflix.repositories.MovieRepository;
import br.com.mateusfilpo.netflix.repositories.UserRepository;
import br.com.mateusfilpo.netflix.services.exceptions.GenreNotFoundException;
import br.com.mateusfilpo.netflix.services.exceptions.InsufficientGenresException;
import br.com.mateusfilpo.netflix.services.exceptions.UserNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository repository;
    private final GenreRepository genreRepository;
    private final MovieRepository movieRepository;

    public UserService(UserRepository repository, GenreRepository genreRepository, MovieRepository movieRepository) {
        this.repository = repository;
        this.genreRepository = genreRepository;
        this.movieRepository = movieRepository;
    }

    public List<UserResponseDTO> findAll() {
        return repository.findAll().stream()
                .map(UserResponseDTO::new)
                .toList();
    }

    public UserResponseDTO findById(Long id) {
        return new UserResponseDTO(repository.findById(id).orElseThrow(() -> new UserNotFoundException(id)));
    }

    public List<MovieWithValueGenreDTO> findRecommendedMovies(Long id) {
        User user = repository.findById(id).orElseThrow(() -> new UserNotFoundException(id));

        Map<Long, Double> userGenresMap = user.getGenres().stream()
                .collect(Collectors.toMap(userGenre -> userGenre.getGenre().getId(), userGenre -> userGenre.getValue()));

        List<Movie> movies = movieRepository.findMoviesByGenreIds(userGenresMap.keySet());

        List<MovieWithValueGenreDTO> recommendedMovies  = movies.stream()
                .map(movie -> new MovieWithValueGenreDTO(movie, calculateDistance(user, movie)))
                .sorted(Comparator.comparingDouble(MovieWithValueGenreDTO::getDistance))
                .toList();

        return recommendedMovies;
    }

    public Long createUser(UserCreateDTO dto) {
        User user = new User(dto);
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

    private void updateData(User user, UserUpdateDTO dto) {
        if (dto.getUsername() != null) {
            user.setUsername(dto.getUsername());
        }

        if (dto.getPassword() != null) {
            user.setPassword(dto.getPassword());
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
            if (dto.getGenres().size() < 3) {
                throw new InsufficientGenresException();
            }
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
