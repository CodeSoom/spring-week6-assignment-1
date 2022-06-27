package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;
import com.codesoom.assignment.dto.UserModificationData;
import com.codesoom.assignment.dto.UserRegistrationData;
import com.codesoom.assignment.errors.UserEmailDuplicationException;
import com.codesoom.assignment.errors.UserNotFoundException;
import com.github.dozermapper.core.Mapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * User 에 대한 비즈니스 로직
 */
@Service
@Transactional
public class UserService {
    /**
     * User 데이터 저장소
     */
    private final Mapper mapper;
    private final UserRepository userRepository;

    public UserService(Mapper dozerMapper, UserRepository userRepository) {
        this.mapper = dozerMapper;
        this.userRepository = userRepository;
    }

    /**
     * 주어진 UserRegistrationData 를 등록하여 등록된 user 를 반환
     *
     * @param registrationData 등록할 user(UserRegistrationData)
     * @return 등록된 user
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
     * 주어진 id 와 일치하는 user 를 수정할 UserModificationData 로 수정하여 반환
     *
     * @param id user 식별자
     * @param modificationData 수정할 user(UserModificationData)
     * @return 수정된 user
     */
    public User updateUser(Long id, UserModificationData modificationData) {
        User user = findUser(id);

        User source = mapper.map(modificationData, User.class);
        user.changeWith(source);

        return user;
    }

    /**
     * 주어진 id 와 일치하는 user 을 삭제
     *
     * @param id user 식별자
     * @return 주어진 `deleted = false` 값을 `deleted = true` 상태로 변환한 user
     * @throws UserNotFoundException user 식별자로 user 를 찾을 수 없는 경우
     */
    public User deleteUser(Long id) {
        User user = findUser(id);
        user.destroy();
        return user;
    }

    /**
     * 주어진 id 와 일치하는 user 가 없을 때 UserNotFoundException 예외 처리 반환
     * 단, 주어진 id 와 일치하는 동시에 `deleted = false` 형태도 일치해야 함
     *
     * @param id user 식별자
     * @return 주어진 id 와 일치하는 user 혹은 UserNotFoundException 예외 처리
     * @throws UserNotFoundException user 식별자로 user 를 찾을 수 없는 경우
     */
    private User findUser(Long id) {
        return userRepository.findByIdAndDeletedIsFalse(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }
}
