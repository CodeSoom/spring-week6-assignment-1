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
 * 회원정보에 대한 등록, 수정, 비활성화, 검색 처리를 담당합니다.
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
     * 회원정보를 등록합니다.
     *
     * @throws UserEmailDuplicationException 해당 Email 정보가 이미 존재합니다.
     *
     * @param registrationData 등록하려는 회원정보
     * @return 등록된 회원정보
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
     * 회원정보를 수정합니다.
     *
     * @param id 수정하려는 회원정보 식별자
     * @param modificationData 새로 수정하려는 회원정보 내용
     * @return 수정된 회원정보
     */
    public User updateUser(Long id, UserModificationData modificationData) {
        User user = findUser(id);

        User source = mapper.map(modificationData, User.class);
        user.changeWith(source);

        return user;
    }

    /**
     * 회원정보를 비활성화 합니다.
     *
     * @param id 비활성화 하려는 회원정보
     * @return 비활성화 된 회원정보
     */
    public User deleteUser(Long id) {
        User user = findUser(id);
        user.destroy();
        return user;
    }

    /**
     * 회원정보를 식별자로 검색합니다.
     *
     * @throws UserNotFoundException 해당 id의 회원정보가 존재하지 않습니다.
     *
     * @param id 검색하려는 회원정보 식별자
     * @return 검색된 회원정보
     */
    private User findUser(Long id) {
        return userRepository.findByIdAndDeletedIsFalse(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    /**
     * 회원정보를 Email로 검색합니다.
     *
     * @throws UserNotFoundException 해당 Email의 회원정보가 존재하지 않습니다.
     *
     * @param email 검색하려는 회원정보 Email
     * @return 검색된 회원정보
     */
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email));
    }
}
