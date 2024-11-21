package br.com.mateusfilpo.netflix.dtos;

import br.com.mateusfilpo.netflix.domain.Genre;

public class GenreDTO {

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
}
