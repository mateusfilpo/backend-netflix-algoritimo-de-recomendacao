package br.com.mateusfilpo.netflix.controllers.exceptions;

import br.com.mateusfilpo.netflix.services.exceptions.GenreNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Date;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(GenreNotFoundException.class)
    public ResponseEntity<StandardError> resourceNotFound(GenreNotFoundException e, HttpServletRequest request) {
        String error = "Genre not found";
        HttpStatus status = HttpStatus.NOT_FOUND;
        StandardError err = new StandardError(System.currentTimeMillis(), status.value(), error, e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }
}
