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

    /**
    * @throws PasswordNotEqualException
     *          source와 target이 일치하지 않은 경우
    */
    private void validatePassword(String target, String source) {
        if (!target.equals(source)) {
            throw new PasswordNotEqualException();
        }
    }

    /**
     * @throws UserNotFoundException
     *          email에 해당하는 User가 없는 경우,
     *          email에 해당하는 User가 삭제된 경우
     */
    private User findUserBy(String email) {
        return repository.findByEmailAndDeletedIsFalse(email)
                .orElseThrow(() -> new UserNotFoundException(email));
    }

}
