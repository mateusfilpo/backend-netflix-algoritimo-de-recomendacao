package br.com.mateusfilpo.netflix.dtos.genres;

import br.com.mateusfilpo.netflix.domain.Genre;
import br.com.mateusfilpo.netflix.services.validations.UniqueGenre;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@UniqueGenre
public class GenreDTO {

    @NotNull(message = "Name cannot be null")
    @NotBlank(message = "Name cannot be blank")
    private String name;

    public GenreDTO() {
    }

    public GenreDTO(String name) {
        this.name = name;
    }

    public GenreDTO(Genre genre) {
        this.name = genre.getName();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
