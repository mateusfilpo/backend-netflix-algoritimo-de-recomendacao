package br.com.mateusfilpo.netflix.services.exceptions;

public class GenreNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public GenreNotFoundException(Long id) {
        super("Genre with id: " + id + " not found");
    }
}
