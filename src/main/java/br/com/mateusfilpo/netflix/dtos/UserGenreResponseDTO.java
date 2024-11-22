package br.com.mateusfilpo.netflix.dtos;

import br.com.mateusfilpo.netflix.domain.MovieGenre;
import br.com.mateusfilpo.netflix.domain.UserGenre;

public class UserGenreResponseDTO {

    private String name;

    public UserGenreResponseDTO() {
    }

    public UserGenreResponseDTO(String name) {
        this.name = name;
    }

    public UserGenreResponseDTO(UserGenre userGenre) {
        this.name = userGenre.getGenre().getName();
    }

    public String getName() {
        return name;
    }
}