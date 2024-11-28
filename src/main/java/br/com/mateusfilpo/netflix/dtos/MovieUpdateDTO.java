package br.com.mateusfilpo.netflix.dtos;

import br.com.mateusfilpo.netflix.services.validations.ConditionalSize;

import java.util.ArrayList;
import java.util.List;

public class MovieUpdateDTO {

    private String title;
    private String description;

    @ConditionalSize(min = 3, message = "The genres list must have at least 3 items if it is not null.")
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
