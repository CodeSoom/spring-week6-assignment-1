package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;
import com.codesoom.assignment.errors.UserNotFoundByEmailException;
import com.codesoom.assignment.errors.WrongUserPasswordException;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User signIn(String email, String password) {
        final User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundByEmailException(email));

        if (!user.getPassword().equals(password)) {
            throw new WrongUserPasswordException();
        }
        return user;
    }
}
