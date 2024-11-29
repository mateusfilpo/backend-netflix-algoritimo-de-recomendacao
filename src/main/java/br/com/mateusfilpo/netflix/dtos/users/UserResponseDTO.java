package br.com.mateusfilpo.netflix.dtos.users;

import br.com.mateusfilpo.netflix.domain.User;
import br.com.mateusfilpo.netflix.dtos.usergenres.UserGenreResponseDTO;

import java.util.ArrayList;
import java.util.List;

public class UserResponseDTO {

    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private List<UserGenreResponseDTO> genres = new ArrayList<>();

    public UserResponseDTO() {
    }

    public UserResponseDTO(String username, String firstName, String lastName, String email) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public UserResponseDTO(User user) {
        this.username = user.getUsername();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        user.getGenres().forEach(userGenre -> getGenres().add(new UserGenreResponseDTO(userGenre)));
    }

    public String getUsername() {
        return username;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public List<UserGenreResponseDTO> getGenres() {
        return genres;
    }
}
