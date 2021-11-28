package com.codesoom.assignment.user.service;

import com.codesoom.assignment.user.domain.User;

public interface UserService {
    User addUser(User user);
    User updateUser(Long id, User user);
    void deleteUserById(Long id);

    User findUserById(Long id);
}
