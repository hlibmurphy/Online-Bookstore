package com.github.onlinebookstore.repositories;

import static com.github.onlinebookstore.model.Role.RoleName;

import com.github.onlinebookstore.model.Role;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName roleName);
}
