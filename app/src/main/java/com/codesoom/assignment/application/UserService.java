package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;
import com.codesoom.assignment.dto.LoginData;
import com.codesoom.assignment.dto.UserModificationData;
import com.codesoom.assignment.dto.UserRegistrationData;
import com.codesoom.assignment.errors.UserPasswordMismatchException;
import com.codesoom.assignment.errors.UserEmailDuplicationException;
import com.codesoom.assignment.errors.UserNotFoundException;
import com.github.dozermapper.core.Mapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * 회원 데이터를 가공하여 반환하거나 처리합니다.
 */
@Service
@Transactional
public class UserService {
    private final Mapper mapper;
    private final UserRepository userRepository;

    public UserService(Mapper dozerMapper, UserRepository userRepository) {
        this.mapper = dozerMapper;
        this.userRepository = userRepository;
    }

    /**
     * 신규 회원을 등록합니다.
     * @param registrationData 등록할 회원 데이터
     * @return user 등록한 회원
     */
    public User registerUser(UserRegistrationData registrationData) {
        String email = registrationData.getEmail();
        if (userRepository.existsByEmail(email)) {
            throw new UserEmailDuplicationException(email);
        }

        User user = mapper.map(registrationData, User.class);
        return userRepository.save(user);
    }

    /**
     * 회원 정보를 갱신합니다.
     * @param id 회원 식별자
     * @param modificationData 갱신할 회원 데이터
     * @return user 갱신한 회원
     */
    public User updateUser(Long id, UserModificationData modificationData) {
        User user = findUser(id);

        User source = mapper.map(modificationData, User.class);
        user.changeWith(source);

        return user;
    }

    /**
     * 회원을 삭제 처리합니다. 회원의 deleted 상태를 true 값으로 변경하는 것을 의미합니다.
     * @param id 회원 식별자
     * @return user 삭제 처리한 회원
     */
    public User deleteUser(Long id) {
        User user = findUser(id);
        user.destroy();
        return user;
    }

    /**
     * ID에 해당하는 회원를 찾습니다.
     * @param id 회원 식별자
     * @return user 회원
     * @throws UserNotFoundException ID가 null이거나 해당하는 회원이 없을 경우.
     */
    private User findUser(Long id) {
        return userRepository.findByIdAndDeletedIsFalse(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    /**
     * 이메일과 패스워드 정보가 일치하는 회원을 찾습니다.
     * @param email 회원 이메일
     * @param password 회원 비밀번호
     * @return user 회원
     */
    public User findUserByEmailPassword(String email, String password) {
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email));

        if (!password.equals(user.getPassword())) {
            throw new UserPasswordMismatchException();
        }

        return user;
    }
}
