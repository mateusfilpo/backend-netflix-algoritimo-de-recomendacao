package br.com.mateusfilpo.netflix.controllers;

import br.com.mateusfilpo.netflix.dtos.movies.MovieWithValueGenreDTO;
import br.com.mateusfilpo.netflix.dtos.users.UserCreateDTO;
import br.com.mateusfilpo.netflix.dtos.users.UserResponseDTO;
import br.com.mateusfilpo.netflix.dtos.users.UserUpdateDTO;
import br.com.mateusfilpo.netflix.services.UserService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> findAll() {
        List<UserResponseDTO> result = service.findAll();

        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> findById(@PathVariable Long id) {
        UserResponseDTO result = service.findById(id);

        return ResponseEntity.ok(result);
    }


    @GetMapping("/{id}/recommended-movies")
    public ResponseEntity<Page<MovieWithValueGenreDTO>> findRecommendedMovies(@PathVariable Long id,
                                                                              @RequestParam(required = false) Integer pageNumber,
                                                                              @RequestParam(required = false) Integer pageSize) {

        Page<MovieWithValueGenreDTO> result = service.findRecommendedMovies(id, pageNumber, pageSize);

        return ResponseEntity.ok(result);
    }


    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserCreateDTO dto) {
        Long id = service.createUser(dto);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(id).toUri();

        return ResponseEntity.created(uri).build();
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateUser(@PathVariable Long id, @Valid @RequestBody UserUpdateDTO dto) {
        service.updateUser(id, dto);

        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        service.deleteUser(id);

        return ResponseEntity.noContent().build();
    }

}
