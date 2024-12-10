package br.com.mateusfilpo.netflix.dtos.users;

import br.com.mateusfilpo.netflix.dtos.usergenres.UserGenreDTO;
import br.com.mateusfilpo.netflix.services.validations.ConditionalSize;
import br.com.mateusfilpo.netflix.services.validations.EmailUpdate;
import br.com.mateusfilpo.netflix.services.validations.UsernameUpdate;
import br.com.mateusfilpo.netflix.services.validations.ValidPassword;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;

@ConditionalSize
@EmailUpdate
@UsernameUpdate
@ValidPassword
public class UserUpdateDTO {

    private String username;

    @NotNull(message = "First name cannot be null.")
    @NotBlank(message = "First name cannot be blank.")
    private String firstName;

    @NotNull(message = "Last name cannot be null.")
    @NotBlank(message = "Last name cannot be blank.")
    private String lastName;

    private String email;

    @NotNull(message = "Phone number cannot be null.")
    @NotBlank(message = "Phone number cannot be blank.")
    private String phoneNumber;

    private String password;

    private List<UserGenreDTO> genres = new ArrayList<>();

    public UserUpdateDTO() {
    }

    public UserUpdateDTO(String username, String firstName, String lastName, String email, String phoneNumber) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<UserGenreDTO> getGenres() {
        return genres;
    }
}
