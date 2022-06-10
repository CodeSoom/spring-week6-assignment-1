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
 * User 관련 비즈니스를 처리하는 Service 클래스
 */
@Service
@Transactional
public class UserService {
    private final Mapper mapper;
    private final UserRepository userRepository;

    /**
     *
     * @param dozerMapper
     * @param userRepository
     */
    public UserService(Mapper dozerMapper, UserRepository userRepository) {
        this.mapper = dozerMapper;
        this.userRepository = userRepository;
    }

    /**
     * 이미 존재하지 않는 email인 경우 User를 저장한 후 저장된 User를 반환한다.
     *
     * @param registrationData 등록할 User의 정보가 담긴 DTO
     * @return 저장된 User
     * @throws UserEmailDuplicationException
     *          email이 이미 존재할 경우
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
     * id로 User를 조회한 후 해당 User의 정보를 수정하고, 수정된 User를 반환한다.
     * 
     * @param id 수정할 User의 id
     * @param modificationData 수정할 정보가 담긴 DTO
     * @return 수정된 User
     */
    public User updateUser(Long id, UserModificationData modificationData) {
        User user = findUser(id);

        User source = mapper.map(modificationData, User.class);
        user.changeWith(source);

        return user;
    }

    /**
     * id로 User를 조회한 후 삭제하고, 삭제된 User를 반환한다.
     * 
     * @param id 삭제할 User의 id
     * @return 삭제된 User
     */
    public User deleteUser(Long id) {
        User user = findUser(id);
        user.destroy();
        return user;
    }

    /**
     * id로 User를 찾고, 찾은 User를 반환한다.
     * 
     * @param id 찾고싶은 User의 id
     * @return 찾은 User
     * @throws UserNotFoundException
     *          id로 User를 찾지 못한 경우
     */
    private User findUser(Long id) {
        return userRepository.findByIdAndDeletedIsFalse(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }
}
