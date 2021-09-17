package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;
import com.codesoom.assignment.dto.UserData;
import com.codesoom.assignment.dto.UserUpdateData;
import com.codesoom.assignment.errors.UserEmailDuplicateException;
import com.codesoom.assignment.errors.UserNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User createUser(UserData source) throws Exception {

        if( emailDuplicateCheck(source.getEmail()) ) {
            throw new UserEmailDuplicateException();
        }

        User user = User.builder()
                .name(source.getName())
                .email(source.getEmail())
                .password(source.getPassword())
                .build();

        return userRepository.save(user);

    }

    @Override
    public User getUser(Long id) {

       return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException());

    }

    @Override
    public User updateUser(Long id, UserUpdateData userUpdateData) {

        User findUser = getUser(id);

        findUser.userUpdate(userUpdateData.getName(),  userUpdateData.getPassword());

        return findUser;

    }

    @Override
    public void deleteUser(Long id) {

        User deleteUser = getUser(id);

        userRepository.deleteById(id);

    }

    @Override
    public boolean emailDuplicateCheck(String mail) {
        return userRepository.existsByEmail(mail);
    }

}
