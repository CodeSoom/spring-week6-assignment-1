package com.codesoom.assignment.application.user.implement;

import com.codesoom.assignment.application.user.UserCommand;
import com.codesoom.assignment.application.user.UserCommandService;
import com.codesoom.assignment.common.exception.UserNotFoundException;
import com.codesoom.assignment.common.mapper.UserMapper;
import com.codesoom.assignment.domain.user.User;
import com.codesoom.assignment.domain.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserCommandServiceImpl implements UserCommandService {
    private final UserRepository userRepository;

    private final UserMapper userMapper;

    public UserCommandServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Transactional
    @Override
    public User createUser(UserCommand.Register command) {
        return userRepository.save(userMapper.toEntity(command));
    }

    /**
     * @throws UserNotFoundException 회원정보가 없을 경우
     */
    @Transactional
    @Override
    public User updateUser(UserCommand.UpdateRequest command) {
        User source = userMapper.toEntity(command);
        User findUser = getFindUser(source.getId());

        findUser.modifyUserInfo(source);

        return findUser;
    }

    /**
     * @throws UserNotFoundException 회원정보가 없을 경우
     */
    @Transactional
    @Override
    public void deleteUser(Long id) {
        User findUser = getFindUser(id);

        userRepository.delete(findUser);
    }

    private User getFindUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }
}
