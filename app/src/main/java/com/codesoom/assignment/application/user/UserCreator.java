package com.codesoom.assignment.application.user;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.dto.UserDto;

public interface UserCreator {
    User createUser(UserDto userDto);
}
