package br.com.mateusfilpo.netflix.dtos.users;

import br.com.mateusfilpo.netflix.dtos.usergenres.UserGenreDTO;
import br.com.mateusfilpo.netflix.services.validations.UniqueEmail;
import br.com.mateusfilpo.netflix.services.validations.UniqueUsername;
import br.com.mateusfilpo.netflix.services.validations.ValidPassword;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;

@UniqueEmail
@UniqueUsername
@ValidPassword
public class UserCreateDTO {

    @NotNull(message = "Username cannot be null.")
    @NotBlank(message = "Username cannot be blank.")
    private String username;

    @NotNull(message = "Password cannot be null.")
    @NotBlank(message = "Password cannot be blank.")
    private String password;

    @NotNull(message = "First name cannot be null.")
    @NotBlank(message = "First name cannot be blank.")
    private String firstName;

    @NotNull(message = "Last name cannot be null.")
    @NotBlank(message = "Last name cannot be blank.")
    private String lastName;

    @Email(message = "Field 'email' is invalid")
    private String email;

    @NotNull(message = "Phone number cannot be null.")
    @NotBlank(message = "Phone number cannot be blank.")
    private String phoneNumber;

    @Size(min = 3, message = "The list of genres must have at least 3 items.")
    private List<UserGenreDTO> genres = new ArrayList<>();

    public UserCreateDTO() {
    }

    public UserCreateDTO(String username, String password, String firstName, String lastName, String email, String phoneNumber) {
        this.username = username;
        this.password = password;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public List<UserGenreDTO> getGenres() {
        return genres;
    }
}
