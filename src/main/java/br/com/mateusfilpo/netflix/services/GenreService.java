package br.com.mateusfilpo.netflix.services;

import br.com.mateusfilpo.netflix.domain.Genre;
import br.com.mateusfilpo.netflix.dtos.GenreDTO;
import br.com.mateusfilpo.netflix.repositories.GenreRepository;
import br.com.mateusfilpo.netflix.services.exceptions.GenreNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GenreService {

    private final GenreRepository repository;

    public GenreService(GenreRepository repository) {
        this.repository = repository;
    }

    public List<GenreDTO> findAllGenres() {
        return repository.findAll().stream()
                .map(GenreDTO::new)
                .toList();
    }

    public GenreDTO findById(Long id) {
        return new GenreDTO(repository.findById(id).orElseThrow(() -> new GenreNotFoundException(id)));
    }

    public Long createGenre(GenreDTO dto) {
        return repository.save(new Genre(dto)).getId();
    }

    public void updateGenre(Long id, GenreDTO dto) {
        if (!repository.existsById(id)) {
            throw new GenreNotFoundException(id);
        }

        Genre genre = repository.getReferenceById(id);
        genre.setName(dto.getName());
        repository.save(genre);
    }

    public void deleteGenre(Long id) {
        if (!repository.existsById(id)) {
            throw new GenreNotFoundException(id);
        }

        repository.deleteById(id);
    }
}
