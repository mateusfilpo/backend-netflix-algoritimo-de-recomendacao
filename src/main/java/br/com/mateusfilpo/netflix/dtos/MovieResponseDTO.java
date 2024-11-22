package br.com.mateusfilpo.netflix.dtos;

import br.com.mateusfilpo.netflix.domain.Movie;

import java.util.ArrayList;
import java.util.List;

public class MovieResponseDTO {

    private String title;
    private String description;
    private List<MovieGenreResponseDTO> genres = new ArrayList<>();

    public MovieResponseDTO() {
    }

    public MovieResponseDTO(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public MovieResponseDTO(Movie movie) {
        this.title = movie.getTitle();
        this.description = movie.getDescription();
        movie.getGenres().forEach(movieGenre -> getGenres().add(new MovieGenreResponseDTO(movieGenre)));
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public List<MovieGenreResponseDTO> getGenres() {
        return genres;
    }
}
