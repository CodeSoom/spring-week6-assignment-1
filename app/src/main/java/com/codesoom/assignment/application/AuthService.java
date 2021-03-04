package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;
import com.codesoom.assignment.errors.WrongUserException;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User signIn(String email, String password)
            throws WrongUserException {
        final User user = userRepository.findByEmail(email)
                .orElseThrow(WrongUserException::new);

        if (!user.getPassword().equals(password)) {
            throw new WrongUserException();
        }
        return user;
    }
}
