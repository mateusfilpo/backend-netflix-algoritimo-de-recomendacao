package br.com.mateusfilpo.netflix.dtos.usergenres;

import jakarta.validation.constraints.NotNull;

public class UserGenreDTO {

    @NotNull(message = "Id cannot be null.")
    private Long id;

    public UserGenreDTO() {
    }

    public UserGenreDTO(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
