package br.com.mateusfilpo.netflix.services;

import br.com.mateusfilpo.netflix.domain.Genre;
import br.com.mateusfilpo.netflix.dtos.genres.GenreDTO;
import br.com.mateusfilpo.netflix.repositories.GenreRepository;
import br.com.mateusfilpo.netflix.services.exceptions.DatabaseException;
import br.com.mateusfilpo.netflix.services.exceptions.GenreNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(SpringExtension.class)
public class GenreServiceTest {

    @InjectMocks
    private GenreService service;

    @Mock
    private GenreRepository repository;

    @Mock
    private CacheManager cacheManager;

    @Mock
    private Cache genreCache;

    @Mock
    private Cache genreListCache;

    private List<Genre> listGenre;
    private long existingId;
    private long nonExistingId;
    private long dependentId;
    private Genre genre;
    private GenreDTO genreDTO;

    @BeforeEach
    void setUp() {
        listGenre = new ArrayList<>();
        existingId = 1L;
        nonExistingId = 999L;
        dependentId = 500L;
        genre = new Genre(existingId, "Ação");
        genreDTO = new GenreDTO();
        genreDTO.setName(genre.getName());

        Mockito.when(repository.findAll()).thenReturn(listGenre);
        Mockito.when(repository.findById(existingId)).thenReturn(Optional.of(genre));
        Mockito.when(repository.findById(nonExistingId)).thenThrow(GenreNotFoundException.class);
        Mockito.when(cacheManager.getCache("genreListCache")).thenReturn(genreListCache);
        Mockito.when(cacheManager.getCache("genreCache")).thenReturn(genreCache);
        Mockito.when(repository.save(any(Genre.class))).thenReturn(genre);
        Mockito.when(repository.existsById(existingId)).thenReturn(true);
        Mockito.when(repository.existsById(nonExistingId)).thenReturn(false);
        Mockito.when(repository.getReferenceById(existingId)).thenReturn(genre);
        Mockito.when(repository.existsById(dependentId)).thenReturn(true);
        Mockito.doNothing().when(repository).deleteById(existingId);
        Mockito.doThrow(EmptyResultDataAccessException.class).when(repository).deleteById(nonExistingId);
        Mockito.doThrow(DataIntegrityViolationException.class).when(repository).deleteById(dependentId);
    }

    @Test
    public void findAllGenresShouldReturnListOfGenreDTOs() {
        List<GenreDTO> result = service.findAllGenres();

        assertNotNull(result);
        Mockito.verify(repository, Mockito.times(1)).findAll();
    }

    @Test
    public void findByIdShouldReturnGenreDTOWhenIdExists() {
        GenreDTO result = service.findById(existingId);

        assertNotNull(result);
        assertEquals(genre.getName(), result.getName());
        Mockito.verify(repository, Mockito.times(1)).findById(existingId);
    }

    @Test
    public void findByIdShouldThrowGenreNotFoundExceptionWhenIdDoesNotExist() {
        assertThrows(GenreNotFoundException.class, () -> service.findById(nonExistingId));
        Mockito.verify(repository, Mockito.times(1)).findById(nonExistingId);
    }

    @Test
    public void createGenreShouldSaveGenre() {
        GenreDTO dto = new GenreDTO();
        dto.setName(genre.getName());

        Long result = service.createGenre(dto);

        Mockito.verify(genreListCache, Mockito.times(1)).clear();
        assertNotNull(result);
        assertEquals(existingId, result);
        Mockito.verify(repository, Mockito.times(1)).save(any(Genre.class));
    }

    @Test
    public void updateGenreShouldUpdateDataWhenIdExists() {
        service.updateGenre(existingId, genreDTO);

        Mockito.verify(genreListCache, Mockito.times(1)).clear();
        Mockito.verify(genreCache, Mockito.times(1)).evict(existingId);

        assertEquals("Ação", genre.getName());

        Mockito.verify(repository, Mockito.times(1)).save(genre);
    }

    @Test
    public void updateGenreShouldThrowGenreNotFoundExceptionWhenIdDoesNotExist() {
        assertThrows(GenreNotFoundException.class, () -> service.updateGenre(nonExistingId, genreDTO));

        Mockito.verify(genreCache, Mockito.times(1)).evict(nonExistingId);
        Mockito.verify(genreListCache, Mockito.times(1)).clear();

        Mockito.verify(repository, Mockito.never()).save(any(Genre.class));
    }

    @Test
    public void deleteGenreShouldDeleteWhenIdExists() {
        service.deleteGenre(existingId);

        Mockito.verify(repository, Mockito.times(1)).deleteById(existingId);
        Mockito.verify(genreCache, Mockito.times(1)).evict(existingId);
        Mockito.verify(genreListCache, Mockito.times(1)).clear();
    }
    @Test
    public void deleteGenreShouldThrowGenreNotFoundExceptionWhenIdDoesNotExist() {
        assertThrows(GenreNotFoundException.class, () -> service.deleteGenre(nonExistingId));

        Mockito.verify(genreCache, Mockito.times(1)).evict(nonExistingId);
        Mockito.verify(genreListCache, Mockito.times(1)).clear();
    }

    @Test
    public void deleteGenreShouldThrowDatabaseExceptionWhenDataIntegrityViolationOccurs() {
        assertThrows(DatabaseException.class, () -> service.deleteGenre(dependentId));

        Mockito.verify(genreCache, Mockito.times(1)).evict(dependentId);
        Mockito.verify(genreListCache, Mockito.times(1)).clear();
        Mockito.verify(repository, Mockito.times(1)).deleteById(dependentId);
    }


}