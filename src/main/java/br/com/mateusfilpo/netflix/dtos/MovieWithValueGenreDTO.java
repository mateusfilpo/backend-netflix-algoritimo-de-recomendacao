package br.com.mateusfilpo.netflix.dtos;

import br.com.mateusfilpo.netflix.domain.Movie;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;

public class MovieWithValueGenreDTO {

    private String title;
    private String description;
    private List<MovieGenreResponseDTO> genres = new ArrayList<>();

    @JsonIgnore
    private Double distance;

    public MovieWithValueGenreDTO() {
    }

    public MovieWithValueGenreDTO(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public MovieWithValueGenreDTO(Movie movie) {
        this.title = movie.getTitle();
        this.description = movie.getDescription();
        movie.getGenres().forEach(movieGenre -> getGenres().add(new MovieGenreResponseDTO(movieGenre)));
    }

    public MovieWithValueGenreDTO(Movie movie, Double distance) {
        this.title = movie.getTitle();
        this.description = movie.getDescription();
        movie.getGenres().forEach(movieGenre -> getGenres().add(new MovieGenreResponseDTO(movieGenre)));
        this.distance = distance;
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

    public Double getDistance() {
        return distance;
    }
}
