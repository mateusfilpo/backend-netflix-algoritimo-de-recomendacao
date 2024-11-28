package br.com.mateusfilpo.netflix.repositories;

import br.com.mateusfilpo.netflix.domain.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

    @Modifying
    @Query("DELETE FROM MovieGenre mg WHERE mg.movie.id = :movieId")
    void deleteMovieGenres(@Param("movieId") Long movieId);

    @Query("SELECT DISTINCT m FROM Movie m " +
           "JOIN MovieGenre mg ON m.id = mg.movie.id " +
           "WHERE mg.genre.id IN :genreIds")
    List<Movie> findMoviesByGenreIds(@Param("genreIds") Set<Long> genreIds);

    @Query("SELECT m FROM Movie m " +
           "JOIN MovieGenre mg ON m.id = mg.movie.id " +
           "WHERE mg.genre.id = :genreId")
    Page<Movie> findAllMoviesByGenre(Long genreId, PageRequest pageRequest);
}
