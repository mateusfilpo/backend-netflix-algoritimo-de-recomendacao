package br.com.mateusfilpo.netflix.services.exceptions;

public class MovieNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public MovieNotFoundException(Long id) {
        super("Movie with id: " + id + " not found");
    }
}
