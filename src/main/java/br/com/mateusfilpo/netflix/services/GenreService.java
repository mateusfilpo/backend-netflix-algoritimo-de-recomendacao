package br.com.mateusfilpo.netflix.services;

import br.com.mateusfilpo.netflix.domain.Genre;
import br.com.mateusfilpo.netflix.dtos.genres.GenreDTO;
import br.com.mateusfilpo.netflix.repositories.GenreRepository;
import br.com.mateusfilpo.netflix.services.exceptions.GenreNotFoundException;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GenreService {

    private final GenreRepository repository;
    private final CacheManager cacheManager;

    public GenreService(GenreRepository repository, CacheManager cacheManager) {
        this.repository = repository;
        this.cacheManager = cacheManager;
    }

    @Cacheable(cacheNames = "genreListCache")
    public List<GenreDTO> findAllGenres() {
        return repository.findAll().stream()
                .map(GenreDTO::new)
                .toList();
    }

    @Cacheable(cacheNames = "genreCache", key = "#id")
    public GenreDTO findById(Long id) {
        return new GenreDTO(repository.findById(id).orElseThrow(() -> new GenreNotFoundException(id)));
    }

    public Long createGenre(GenreDTO dto) {
        if (cacheManager.getCache("genreListCache") != null) {
            cacheManager.getCache("genreListCache").clear();
        }
        return repository.save(new Genre(dto)).getId();
    }

    public void updateGenre(Long id, GenreDTO dto) {
        clearCache(id);

        if (!repository.existsById(id)) {
            throw new GenreNotFoundException(id);
        }

        Genre genre = repository.getReferenceById(id);
        genre.setName(dto.getName());
        repository.save(genre);
    }

    public void deleteGenre(Long id) {
        clearCache(id);

        if (!repository.existsById(id)) {
            throw new GenreNotFoundException(id);
        }

        repository.deleteById(id);
    }

    private void clearCache(Long id) {
        if (cacheManager.getCache("genreCache") != null) {
            cacheManager.getCache("genreCache").evict(id);
        }
        if (cacheManager.getCache("genreListCache") != null) {
            cacheManager.getCache("genreListCache").clear();
        }
    }
}
