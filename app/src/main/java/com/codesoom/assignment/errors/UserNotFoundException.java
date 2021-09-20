package com.codesoom.assignment.errors;

import java.util.NoSuchElementException;

public class UserNotFoundException extends NoSuchElementException  {

    public UserNotFoundException() {
        super("해당 유저를 찾을 수 없습니다.");
    }

}

