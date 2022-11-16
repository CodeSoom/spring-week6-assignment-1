package com.codesoom.assignment.domain.user.infra;

import com.codesoom.assignment.domain.user.domain.User;
import com.codesoom.assignment.domain.user.domain.UserRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaUserRepository
        extends UserRepository, JpaRepository<User, Long> {
    User save(User user);

    boolean existsByEmail(String email);

    Optional<User> findById(Long id);

    Optional<User> findByIdAndDeletedIsFalse(Long id);

    Optional<User> findByEmail(String email);
}
