package br.com.mateusfilpo.netflix.dtos.moviegenres;

public class MovieGenreDTO {

    private Long id;
    private Double value;

    public MovieGenreDTO() {
    }

    public MovieGenreDTO(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public Double getValue() {
        return value;
    }
}
