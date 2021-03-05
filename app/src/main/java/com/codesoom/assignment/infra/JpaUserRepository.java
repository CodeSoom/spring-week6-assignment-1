package com.codesoom.assignment.infra;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface JpaUserRepository
        extends UserRepository, CrudRepository<User, Long> {
    List<User> findAll();
    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    User save(User user);

    void delete(User user);

    Optional<User> findByIdAndDeletedIsFalse(Long id);
}
