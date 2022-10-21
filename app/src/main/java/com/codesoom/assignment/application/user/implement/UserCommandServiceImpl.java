package com.codesoom.assignment.application.user.implement;

import com.codesoom.assignment.application.user.UserCommand;
import com.codesoom.assignment.application.user.UserCommandService;
import com.codesoom.assignment.common.exception.EntityNotFoundException;
import com.codesoom.assignment.common.mapper.UserMapper;
import com.codesoom.assignment.common.response.ErrorCode;
import com.codesoom.assignment.domain.user.User;
import com.codesoom.assignment.domain.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.codesoom.assignment.common.response.ErrorCode.USER_NOT_FOUND;

@Service
@Transactional
public class UserCommandServiceImpl implements UserCommandService {
    private final UserRepository userRepository;

    private final UserMapper userMapper;

    public UserCommandServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public User createUser(UserCommand.Register command) {
        return userRepository.save(userMapper.toEntity(command));
    }

    /**
     * @throws EntityNotFoundException 회원정보가 없을 경우
     */
    @Override
    public User updateUser(UserCommand.UpdateRequest command) {
        User source = userMapper.toEntity(command);
        User findUser = getFindUser(source.getId());

        findUser.modifyUserInfo(source);

        return findUser;
    }

    /**
     * @throws EntityNotFoundException 회원정보가 없을 경우
     */
    @Override
    public void deleteUser(Long id) {
        User findUser = getFindUser(id);

        userRepository.delete(findUser);
    }

    private User getFindUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND));
    }
}
