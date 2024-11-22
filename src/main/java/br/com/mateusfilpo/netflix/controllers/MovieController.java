package br.com.mateusfilpo.netflix.controllers;

import br.com.mateusfilpo.netflix.dtos.MovieCreateDTO;
import br.com.mateusfilpo.netflix.dtos.MovieResponseDTO;
import br.com.mateusfilpo.netflix.dtos.MovieUpdateDTO;
import br.com.mateusfilpo.netflix.services.MovieService;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<MovieResponseDTO>> findAll() {
        List<MovieResponseDTO> result = service.findAllMovies();

        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovieResponseDTO> findById(@PathVariable Long id) {
        MovieResponseDTO result = service.findById(id);

        return ResponseEntity.ok(result);
    }

    @PostMapping
    public ResponseEntity<Void> createMovie(@RequestBody MovieCreateDTO movieCreateDTO) {
        Long id = service.createMovie(movieCreateDTO);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(id).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateMovie(@PathVariable Long id, @RequestBody MovieUpdateDTO movieUpdateDTO) {
        service.updateMovie(id, movieUpdateDTO);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMovie(@PathVariable Long id) {
        service.deleteMovie(id);

        return ResponseEntity.noContent().build();
    }

}
