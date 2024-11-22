package br.com.mateusfilpo.netflix.services.exceptions;

public class InsufficientGenresException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public InsufficientGenresException() {
        super("The genre list must contain at least 3 genres.");
    }
}
