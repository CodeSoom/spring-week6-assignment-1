package com.codesoom.assignment.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FakeUserRepository implements UserRepository {
    private final List<User> users = new ArrayList<>();

    @Override
    public User save(User source) {
        final User user = new User(
                generateID(),
                source.getEmail(),
                source.getName(),
                source.getPassword(),
                false
        );

        users.add(user);
        return user;
    }

    @Override
    public boolean existsByEmail(String email) {
        return false;
    }

    @Override
    public Optional<User> findById(Long id) {
        if (id > users.size()) {
            return Optional.empty();
        }
        return Optional.of(users.get(id.intValue() - 1));
    }

    @Override
    public Optional<User> findByIdAndDeletedIsFalse(Long id) {
        return Optional.empty();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return users.stream()
                .filter((User user) -> user.getEmail().equals(email))
                .findFirst();
    }

    private Long generateID() {
        return Integer.toUnsignedLong(users.size() + 1);
    }
}
