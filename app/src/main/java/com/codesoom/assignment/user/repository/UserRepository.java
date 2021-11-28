package com.codesoom.assignment.user.repository;

import com.codesoom.assignment.user.domain.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    User save(User user);
    Optional<User> findById(Long id);
    void deleteById(Long id);
}
