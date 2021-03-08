package com.codesoom.assignment.domain;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findById(Long id);

    User save(User user);

    void delete(User user);

    boolean existsByEmail(String email);
}
