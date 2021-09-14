package com.codesoom.assignment.fake;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class InMemoryUserRepository implements UserRepository {
    private static final InMemoryUserRepository instance = new InMemoryUserRepository();
    private static final Map<Long, User> store = new HashMap<>();
    private static Long sequence = 0L;

    private InMemoryUserRepository() {}

    public static InMemoryUserRepository getInstance() {
        return instance;
    }

    @Override
    public User save(User user) {
        sequence++;

        final User newUser = User.builder()
                .id(sequence).name(user.getName())
                .email(user.getEmail())
                .password(user.getPassword())
                .build();

        store.put(sequence, newUser);

        return newUser;
    }

    @Override
    public boolean existsByEmail(String email) {
        return store.values()
                .stream()
                .anyMatch(user -> user.getEmail().equals(email));
    }

    @Override
    public Optional<User> findById(Long id) {
        return Optional.of(store.get(id));
    }

    @Override
    public Optional<User> findByIdAndDeletedIsFalse(Long id) {
        final User user = store.get(id);

        if (Objects.isNull(user) || user.isDeleted()) {
            return Optional.empty();
        }

        return Optional.of(user);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return store.values()
                .stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst();
    }

    @Override
    public Optional<User> findByEmailAndDeletedIsFalse(String email) {
        return findByEmail(email)
                .filter(user-> !user.isDeleted());
    }

    @Override
    public void deleteAll() {
        store.clear();
        sequence = 0L;
    }
}
