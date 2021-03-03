package com.codesoom.assignment.infra;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryUserRepository implements UserRepository {
    private final Map<Long, User> map = new HashMap<>();

    @Override
    public User save(User user) {
        return map.put(user.getId(), user);
    }

    @Override
    public boolean existsByEmail(String email) {
        return map.values()
            .stream()
            .anyMatch(
                user -> user.getEmail().equals(email)
            );
    }

    @Override
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(map.get(id));
    }

    @Override
    public Optional<User> findByIdAndDeletedIsFalse(Long id) {
        User user = map.get(id);
        if (user == null) {
            return Optional.empty();
        }
        if (user.isDestroy()) {
            return Optional.empty();
        }
        return Optional.of(user);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return map.values()
            .stream()
            .filter(
                user -> user.getEmail().equals(email)
            ).findAny();
    }
}
