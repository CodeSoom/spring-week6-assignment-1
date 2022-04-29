package com.codesoom.assignment.application.user;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.dto.UserDto;

public interface UserChanger {
    void deleteById(Long id);
    User save(Long id, UserDto userDto);
}
