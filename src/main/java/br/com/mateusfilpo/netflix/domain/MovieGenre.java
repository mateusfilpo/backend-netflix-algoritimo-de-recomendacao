package br.com.mateusfilpo.netflix.domain;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "tb_movie_genre")
public class MovieGenre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "movie_id")
    private Movie movie;

    @ManyToOne
    @JoinColumn(name = "genre_id")
    private Genre genre;

    private Double _value;

    public MovieGenre() {
    }

    public MovieGenre(Long id, Movie movie, Genre genre, Double _value) {
        this.id = id;
        this.movie = movie;
        this.genre = genre;
        this._value = _value;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public Double get_value() {
        return _value;
    }

    public void set_value(Double _value) {
        this._value = _value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MovieGenre that = (MovieGenre) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "MovieGenre{" +
                "id=" + id +
                ", movie=" + movie +
                ", genre=" + genre +
                ", _value=" + _value +
                '}';
    }
}