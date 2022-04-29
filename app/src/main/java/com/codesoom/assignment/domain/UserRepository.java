package com.codesoom.assignment.domain;

import com.codesoom.assignment.dto.UserDto;
import org.springframework.context.annotation.Primary;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

@Primary
public interface UserRepository extends CrudRepository<User, Long> {

    List<User> findAll();

    Optional<User> findById(Long id);

    User save(User user);

    default void deleteById(Long id) {
        Optional<User> user = findById(id);
        user.ifPresent(this::delete);
    }
}
