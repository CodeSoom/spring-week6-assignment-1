package com.codesoom.assignment.domain;

import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface UserRepository {
    User save(User user);

    boolean existsByEmail(String email);

    Optional<User> findById(Long id);

    Optional<User> findByIdAndDeletedIsFalse(Long id);

    Optional<User> findByEmail(String email);

    Optional<User> findByEmailAndPassword(String email, String password);
}
