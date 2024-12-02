package br.com.mateusfilpo.netflix.repositories;

import br.com.mateusfilpo.netflix.domain.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    @Test
    void shouldReturnRoleWhenFindByAuthorityIsCalled() {
        final String ROLE_NAME = "ROLE_USER";

        Role result = roleRepository.findByAuthority(ROLE_NAME);

        assertNotNull(result);
        assertEquals(ROLE_NAME, result.getAuthority());
    }
}