package br.com.mateusfilpo.netflix.dtos;

import br.com.mateusfilpo.netflix.domain.User;

import java.util.ArrayList;
import java.util.List;

public class UserUpdateDTO {

    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private List<UserGenreDTO> genres = new ArrayList<>();

    public UserUpdateDTO() {
    }

    public UserUpdateDTO(String username, String firstName, String lastName, String email) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
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

    public String getPassword() {
        return password;
    }

    public List<UserGenreDTO> getGenres() {
        return genres;
    }
}
