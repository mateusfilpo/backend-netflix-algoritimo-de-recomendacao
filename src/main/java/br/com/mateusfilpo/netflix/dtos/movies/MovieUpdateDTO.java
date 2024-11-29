package br.com.mateusfilpo.netflix.dtos.movies;

import br.com.mateusfilpo.netflix.dtos.moviegenres.MovieGenreDTO;
import br.com.mateusfilpo.netflix.services.validations.ConditionalSize;

import java.util.ArrayList;
import java.util.List;

@ConditionalSize
public class MovieUpdateDTO {

    private String title;
    private String description;

    private List<MovieGenreDTO> genres = new ArrayList<>();

    public MovieUpdateDTO() {
    }

    public MovieUpdateDTO(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public List<MovieGenreDTO> getGenres() {
        return genres;
    }
}
