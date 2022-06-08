package com.codesoom.assignment.application;

import com.codesoom.assignment.dto.SessionResponseData;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    public SessionResponseData login() {
        return SessionResponseData.builder().accessToken(".").build();
    }
}
