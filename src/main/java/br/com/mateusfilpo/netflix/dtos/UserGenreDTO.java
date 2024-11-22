package br.com.mateusfilpo.netflix.dtos;

public class UserGenreDTO {

    private Long id;
    private Double value;

    public UserGenreDTO() {
    }

    public UserGenreDTO(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public Double getValue() {
        return value;
    }
}
