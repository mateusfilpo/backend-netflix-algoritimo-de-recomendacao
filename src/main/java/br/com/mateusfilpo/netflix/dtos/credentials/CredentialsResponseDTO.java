package br.com.mateusfilpo.netflix.dtos.credentials;

import br.com.mateusfilpo.netflix.dtos.roles.RoleDTO;

import java.util.HashSet;
import java.util.Set;

public class CredentialsResponseDTO {

    private String username;
    private String password;
    private Set<RoleDTO> roles = new HashSet<>();

    public CredentialsResponseDTO() {
    }

    public CredentialsResponseDTO(String username, String password) {
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

    public Set<RoleDTO> getRoles() {
        return roles;
    }
}
