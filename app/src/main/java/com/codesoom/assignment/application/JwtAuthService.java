package com.codesoom.assignment.application;

import com.codesoom.assignment.auth.ClaimTokenAuth;
import com.codesoom.assignment.auth.JwtAuth;
import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;
import com.codesoom.assignment.errors.PasswordNotEqualException;
import com.codesoom.assignment.errors.UserNotFoundException;
import com.codesoom.assignment.infra.JpaUserRepository;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Service;

@Service
public class JwtAuthService implements TokenAuthService {
    private final ClaimTokenAuth<Claims> auth;
    private final UserRepository repository;

    public JwtAuthService(JwtAuth auth, JpaUserRepository repository) {
        this.auth = auth;
        this.repository = repository;
    }

    @Override
    public String login(String email, String password) {
        User user = findUserBy(email);

        validatePassword(password, user.getPassword());

        return auth.encode(user.getId());
    }

    private void validatePassword(String target, String source) {
        if (!target.equals(source)) {
            throw new PasswordNotEqualException();
        }
    }

    private User findUserBy(String email) {
        return repository.findByEmailAndDeletedIsFalse(email)
                .orElseThrow(() -> new UserNotFoundException(email));
    }


}
