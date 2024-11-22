package br.com.mateusfilpo.netflix.dtos;

import br.com.mateusfilpo.netflix.domain.MovieGenre;

public class MovieGenreResponseDTO {

    private String name;

    public MovieGenreResponseDTO() {
    }

    public MovieGenreResponseDTO(String name) {
        this.name = name;
    }

    public MovieGenreResponseDTO(MovieGenre movieGenre) {
        this.name = movieGenre.getGenre().getName();
    }

    public String getName() {
        return name;
    }
}
