package com.codesoom.assignment.infra;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InMemoryUserRepository implements UserRepository {

    private final List<User> users = new ArrayList<>();
    private Long newId = 1L;

    @Override
    public User save(User user) {
        User newUser = new User(newId++, user.getEmail(), user.getName(), user.getPassword(), false);
        users.add(newUser);
        return newUser;
    }

    @Override
    public boolean existsByEmail(String email) {
        return users.stream()
                .anyMatch(user -> {
                    return user.getEmail().equals(email);
                });
    }

    @Override
    public Optional<User> findById(Long id) {
        return users.stream()
                .filter(user -> {
                    return user.getId().equals(id);
                })
                .findFirst();
    }

    @Override
    public Optional<User> findByIdAndDeletedIsFalse(Long id) {
        return findById(id).flatMap(user -> {
            return user.isDeleted() ? Optional.empty() : Optional.of(user);
        });
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return users.stream()
                .filter(user -> {
                    return user.getEmail().equals(email);
                })
                .findFirst();
    }
}
