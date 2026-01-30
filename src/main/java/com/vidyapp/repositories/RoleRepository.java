package com.vidyapp.repositories;

import com.vidyapp.models.AppRole;
import com.vidyapp.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRoleName(AppRole appRole);
}
