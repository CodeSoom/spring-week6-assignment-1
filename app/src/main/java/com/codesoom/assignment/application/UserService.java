package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;
import com.codesoom.assignment.dto.request.UserModificationData;
import com.codesoom.assignment.dto.request.UserRegistrationData;
import com.codesoom.assignment.exception.UserEmailDuplicationException;
import com.codesoom.assignment.exception.UserNotFoundException;
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
     * 회원을 저장합니다.
     * @param registrationData 저장할 회원
     * @return 저장된 회원
     * @throws UserEmailDuplicationException 이메일이 기존에 존재하는 경우
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
     * 회원을 수정합니다.
     * @param id 수정할 회원 아이디
     * @param modificationData 수정할 회원
     * @return 수정된 회원
     * @throws UserNotFoundException 수정할 회원을 찾지 못한 경우
     */
    public User updateUser(Long id, UserModificationData modificationData) {
        User user = findUser(id);

        User source = mapper.map(modificationData, User.class);
        user.changeWith(source);

        return user;
    }

    /**
     * 회원을 삭제합니다.
     * @param id 삭제할 회원 아이디
     * @return 삭제된 회원
     * @throws UserNotFoundException 삭제할 회원을 찾지 못한 경우
     */
    public User deleteUser(Long id) {
        User user = findUser(id);
        user.destroy();
        return user;
    }

    /**
     * 회원을 조회하여 반환합니다.
     * @param id 조회할 회원 아이디
     * @return 조회된 회원
     * @throws UserNotFoundException 회원을 찾지 못한 경우
     */
    private User findUser(Long id) {
        return userRepository.findByIdAndDeletedIsFalse(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    /**
     * 회원을 조회합니다.
     * @param email 조회할 이메일
     * @return 조회된 회원
     * @throws UserNotFoundException 회원을 찾지 못한 경우
     */
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email + " : 이메일을 가진 회원 조회에 실패했습니다."));
    }
}
