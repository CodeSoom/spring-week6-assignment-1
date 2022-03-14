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
     *
     *
     * @param registrationData
     * @return
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
     *  id에 해당하는 user를 수정하고 리턴한다.
     *
     * @param userId user의 id
     * @param modificationData 수정할 사용자 데이터
     * @return 수정된 user
     */
    public User updateUser(Long userId, UserModificationData modificationData) {
        User user = findUser(userId);

        User source = mapper.map(modificationData, User.class);
        user.changeWith(source);

        return user;
    }

    /**
     * id에 해당하는 user을 삭제하고 리턴한다.
     *
     * @param userId user의 id
     */
    public User deleteUser(Long userId) {
        User user = findUser(userId);
        user.destroy();
        return user;
    }

    /**
     * id에 해당하는 user을 찾고 없다면 예외를 던진다.
     *
     * @param userId user의 id
     * @return user의 정보
     * @throws UserNotFoundException 예외를 던진다.
     */
    private User findUser(Long userId) {
        return userRepository.findByIdAndDeletedIsFalse(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }
}
