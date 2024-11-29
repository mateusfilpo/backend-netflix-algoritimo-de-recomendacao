package br.com.mateusfilpo.netflix.dtos.users;

import br.com.mateusfilpo.netflix.dtos.usergenres.UserGenreDTO;
import br.com.mateusfilpo.netflix.services.validations.ConditionalSize;
import br.com.mateusfilpo.netflix.services.validations.EmailUpdate;
import br.com.mateusfilpo.netflix.services.validations.UsernameUpdate;
import br.com.mateusfilpo.netflix.services.validations.ValidPassword;

import java.util.ArrayList;
import java.util.List;

@ConditionalSize
@EmailUpdate
@UsernameUpdate
@ValidPassword
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
