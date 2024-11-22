package br.com.mateusfilpo.netflix.domain;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "tb_user_genre")
public class UserGenre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "genre_id")
    private Genre genre;

    private Double _value;

    public UserGenre() {
    }

    public UserGenre(Long id, User user, Genre genre, Double _value) {
        this.id = id;
        this.user = user;
        this.genre = genre;
        this._value = _value;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public Double get_Value() {
        return _value;
    }

    public void set_Value(Double _value) {
        this._value = _value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserGenre that = (UserGenre) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "UsuarioGenero{" +
                "id=" + id +
                ", user=" + user +
                ", genre=" + genre +
                ", _value=" + _value +
                '}';
    }
}
