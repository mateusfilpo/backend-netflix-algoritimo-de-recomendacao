package br.com.mateusfilpo.netflix.repositories;

import br.com.mateusfilpo.netflix.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Modifying
    @Query("DELETE FROM UserGenre mg WHERE mg.user.id = :userId")
    void deleteUserGenres(@Param("userId") Long userId);
}
