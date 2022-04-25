package com.codesoom.assignment.domain;

import com.codesoom.assignment.application.UserSaveRequest;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository<User, Long> {

    @Override
    List<User> findAll();

    default User save(UserSaveRequest userSaveRequest) {
        return save(userSaveRequest.user());
    }

}
