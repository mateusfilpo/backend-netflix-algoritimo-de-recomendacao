package br.com.mateusfilpo.netflix.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CredentialsRequestDTO {

    @NotNull(message = "Username cannot be null.")
    @NotBlank(message = "Username cannot be blank.")
    private String username;

    @NotNull(message = "Password cannot be null.")
    @NotBlank(message = "Password cannot be blank.")
    private String password;

    public CredentialsRequestDTO() {
    }

    public CredentialsRequestDTO(String username, String password) {
        this.username = username;
        this.password = password;
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
}
