package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;
import com.codesoom.assignment.dto.UserRegistrationData;
import com.codesoom.assignment.dto.UserResultData;
import com.codesoom.assignment.errors.UserEmailDuplicationException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * 유저를 생성하고 유저 결과 정보를 리턴합니다.
     *
     * @param data 로그인 정보
     * @return 유저 결과 정보
     * @throws UserEmailDuplicationException 이메일이 중복된 경우
     */
    public UserResultData registerUser(UserRegistrationData data) {
        String email = data.getEmail();

        if (userRepository.existsByEmail(email)) {
            throw new UserEmailDuplicationException(email);
        }

        User user = userRepository.save(data.toUser());
        return UserResultData.from(user);
    }
}
