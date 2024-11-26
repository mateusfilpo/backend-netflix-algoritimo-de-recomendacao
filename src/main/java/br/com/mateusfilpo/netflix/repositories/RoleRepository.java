package br.com.mateusfilpo.netflix.repositories;

import br.com.mateusfilpo.netflix.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByAuthority(String authority);
}
