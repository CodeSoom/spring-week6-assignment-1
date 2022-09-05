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
     * 사용자를 등록한다.
     *
     * @param registrationData 등록할 유저의 정보
     * @throws UserEmailDuplicationException 사용자의 Email이 중복되었을 경우
     * @return 저장된 유저의 정보
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
     * 사용자를 수정한다.
     *
     * @param id 수정할 사용자의 식별자
     * @param modificationData 수정할 사용자의 정보
     * @throws UserNotFoundException 식별자에 해당하는 사용자가 존재하지 않을 경우
     * @return 수정된 사용자
     */
    public User updateUser(Long id, UserModificationData modificationData) {
        User user = findUser(id);

        User source = mapper.map(modificationData, User.class);
        user.changeWith(source);

        return user;
    }

    /**
     * 사용자를 삭제한다.
     *
     * @param id 삭제할 사용자의 식별자
     * @throws UserNotFoundException 식별자에 해당하는 사용자가 존재하지 않을 경우
     * @return 삭제된 사용자
     */
    public User deleteUser(Long id) {
        User user = findUser(id);
        user.destroy();
        return user;
    }

    /**
     * 사용자를 조회한다.
     *
     * @param id 조회할 사용자의 식별자
     * @throws UserNotFoundException 식별자에 해당하는 사용자가 존재하지 않을 경우
     * @return 식별자에 해당하는 사용자
     */
    private User findUser(Long id) {
        return userRepository.findByIdAndDeletedIsFalse(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }
}
