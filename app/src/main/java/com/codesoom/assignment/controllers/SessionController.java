package com.codesoom.assignment.controllers;

import com.codesoom.assignment.dto.LoginRequestData;
import com.codesoom.assignment.dto.LoginResponseData;

public interface SessionController {
    LoginResponseData login(LoginRequestData requestData);
}
