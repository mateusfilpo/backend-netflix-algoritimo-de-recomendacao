package br.com.mateusfilpo.netflix.controllers.exceptions;

import br.com.mateusfilpo.netflix.services.exceptions.GenreNotFoundException;
import br.com.mateusfilpo.netflix.services.exceptions.InsufficientGenresException;
import br.com.mateusfilpo.netflix.services.exceptions.MovieNotFoundException;
import br.com.mateusfilpo.netflix.services.exceptions.UserNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Date;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(GenreNotFoundException.class)
    public ResponseEntity<StandardError> genreNotFound(GenreNotFoundException e, HttpServletRequest request) {
        String error = "Genre not found";
        HttpStatus status = HttpStatus.NOT_FOUND;
        StandardError err = new StandardError(System.currentTimeMillis(), status.value(), error, e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(MovieNotFoundException.class)
    public ResponseEntity<StandardError> movieNotFound(MovieNotFoundException e, HttpServletRequest request) {
        String error = "Movie not found";
        HttpStatus status = HttpStatus.NOT_FOUND;
        StandardError err = new StandardError(System.currentTimeMillis(), status.value(), error, e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<StandardError> movieNotFound(UserNotFoundException e, HttpServletRequest request) {
        String error = "User not found";
        HttpStatus status = HttpStatus.NOT_FOUND;
        StandardError err = new StandardError(System.currentTimeMillis(), status.value(), error, e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(InsufficientGenresException.class)
    public ResponseEntity<StandardError> movieNotFound(InsufficientGenresException e, HttpServletRequest request) {
        String error = "Insufficient number of genres provided";
        HttpStatus status = HttpStatus.BAD_REQUEST;
        StandardError err = new StandardError(System.currentTimeMillis(), status.value(), error, e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }

}
