package com.codesoom.assignment.domain;

import java.util.Optional;

public interface UserRepository {
    User save(User user);

    boolean existsByEmail(String email);

    Optional<User> findById(Long id);

    Optional<User> findByIdAndDeletedIsFalse(Long id);

    Optional<User> findByEmail(String email);

    Optional<User> findByEmailAndDeletedIsFalse(String email);

    void deleteAll();

    default Optional<User> findByEmailAndDeletedIsFalse(EmailSupplier supplier) {
        return this.findByEmailAndDeletedIsFalse(supplier.getEmail());
    }
}
