package com.codesoom.assignment.domain.users;

import com.codesoom.assignment.application.users.UserSaveRequest;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {

    @Override
    List<User> findAll();

    default User save(UserSaveRequest userSaveRequest) {
        return save(userSaveRequest.user());
    }

    Optional<User> findByEmail(String email);

}
