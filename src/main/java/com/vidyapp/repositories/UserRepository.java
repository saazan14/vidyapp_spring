package com.vidyapp.repositories;

import com.vidyapp.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserName(String username);

    boolean existsByUserName(String username);

    boolean existsByEmail(String email);
}
