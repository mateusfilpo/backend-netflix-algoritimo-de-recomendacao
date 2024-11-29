package br.com.mateusfilpo.netflix.repositories;

import br.com.mateusfilpo.netflix.domain.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GenreRepository extends JpaRepository<Genre, Long> {
    boolean existsByName(String genre);
}
