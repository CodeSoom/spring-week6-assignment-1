package com.codesoom.assignment.application.user;

import com.codesoom.assignment.domain.User;

import java.util.List;

public interface UserSelector {
    User user(Long id);

    List<User> users();
}
