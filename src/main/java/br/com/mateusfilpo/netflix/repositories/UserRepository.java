package br.com.mateusfilpo.netflix.repositories;

import br.com.mateusfilpo.netflix.domain.User;
import br.com.mateusfilpo.netflix.domain.UserGenre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Modifying
    @Query("DELETE FROM UserGenre ug WHERE ug.user.id = :userId")
    void deleteUserGenres(@Param("userId") Long userId);

    @Query("SELECT ug FROM UserGenre ug WHERE ug.user.id = :userId")
    List<UserGenre> findUserGenresByUserId(@Param("userId") Long userId);

    User findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}
