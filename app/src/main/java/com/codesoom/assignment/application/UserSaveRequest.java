package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.User;


/**
 * 회원 생성 요청.
 */
public interface UserSaveRequest {

    String getName();

    String getEmail();

    String getPassword();

    default User user() {
        return new User(getName(), getEmail(), getPassword());
    }

}
