package br.com.mateusfilpo.netflix.controllers;

import br.com.mateusfilpo.netflix.domain.User;
import br.com.mateusfilpo.netflix.dtos.CredentialsRequestDTO;
import br.com.mateusfilpo.netflix.dtos.CredentialsResponseDTO;
import br.com.mateusfilpo.netflix.dtos.RoleDTO;
import br.com.mateusfilpo.netflix.services.UserService;
import jakarta.validation.Valid;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/validate-credentials")
public class AuthenticationController {

    private UserService service;
    private PasswordEncoder passwordEncoder;

    public AuthenticationController(UserService service, PasswordEncoder passwordEncoder) {
        this.service = service;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping
    public CredentialsResponseDTO validateCredentials(@Valid @RequestBody CredentialsRequestDTO credentials) {
        User user = (User) service.loadUserByUsername(credentials.getUsername());

        if (user != null && passwordEncoder.matches(credentials.getPassword(), user.getPassword())) {
            CredentialsResponseDTO responseDTO = new CredentialsResponseDTO();
            responseDTO.setUsername(user.getUsername());
            responseDTO.setPassword(user.getPassword());
            user.getAuthorities().forEach(authority -> responseDTO.getRoles().add(new RoleDTO(authority.getAuthority())));

            return responseDTO;
        }
        return null;
    }
}