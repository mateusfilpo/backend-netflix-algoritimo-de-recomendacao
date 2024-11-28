package br.com.mateusfilpo.netflix.controllers;

import br.com.mateusfilpo.netflix.dtos.MovieCreateDTO;
import br.com.mateusfilpo.netflix.dtos.MovieResponseDTO;
import br.com.mateusfilpo.netflix.dtos.MovieUpdateDTO;
import br.com.mateusfilpo.netflix.services.MovieService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/movies")
public class MovieController {

    private final MovieService service;

    public MovieController(MovieService service) {
        this.service = service;
    }


    @GetMapping
    public ResponseEntity<Page<MovieResponseDTO>> findAll(@RequestParam(required = false) Integer pageNumber,
                                                          @RequestParam(required = false) Integer pageSize) {

        Page<MovieResponseDTO> result = service.findAllMovies(pageNumber, pageSize);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovieResponseDTO> findById(@PathVariable Long id) {
        MovieResponseDTO result = service.findById(id);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/genre/{genre-id}")
    public ResponseEntity<Page<MovieResponseDTO>> findByGenre(@PathVariable("genre-id") Long genreId,
                                                              @RequestParam(required = false) Integer pageNumber,
                                                              @RequestParam(required = false) Integer pageSize) {

        Page<MovieResponseDTO> result = service.findAllMoviesByGenre(genreId, pageNumber, pageSize);

        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Void> createMovie(@Valid @RequestBody MovieCreateDTO movieCreateDTO) {
        Long id = service.createMovie(movieCreateDTO);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(id).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateMovie(@PathVariable Long id, @Valid @RequestBody MovieUpdateDTO movieUpdateDTO) {
        service.updateMovie(id, movieUpdateDTO);

        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMovie(@PathVariable Long id) {
        service.deleteMovie(id);

        return ResponseEntity.noContent().build();
    }

}
