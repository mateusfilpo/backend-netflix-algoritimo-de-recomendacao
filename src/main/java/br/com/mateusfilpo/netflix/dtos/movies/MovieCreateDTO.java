package br.com.mateusfilpo.netflix.dtos.movies;

import br.com.mateusfilpo.netflix.dtos.moviegenres.MovieGenreDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;

public class MovieCreateDTO  {

    @NotNull(message = "Title cannot be null.")
    @NotBlank(message = "Title cannot be blank.")
    private String title;

    @NotNull(message = "Description cannot be null.")
    @NotBlank(message = "Description cannot be blank.")
    private String description;

    @Size(min = 3, message = "The list of genres must have at least 3 items.")
    private List<MovieGenreDTO> genres = new ArrayList<>();

    public MovieCreateDTO () {
    }

    public MovieCreateDTO (String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<MovieGenreDTO> getGenres() {
        return genres;
    }
}
