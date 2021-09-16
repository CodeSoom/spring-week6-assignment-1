package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.dto.UserData;
import com.codesoom.assignment.errors.UserNotFoundException;
import org.springframework.stereotype.Service;

/**
 * 사용자 생성, 수정, 삭제 기능을 담당하는 클래스
 */
@Service
public interface UserService {

    /**
     * 사용자를 생성하고, 생성된 사용자 정보를 리턴합니다.
     * @param source 생성할 사용자 정보
     * @return 생성된 사용자
     */
    User createUser(UserData source) throws Exception;

    /**
     * 사용자 정보를 수정하고 수정된 사용자 정보를 리턴합니다.
     * @param id 사용자 id
     * @param source 수정할 사용자 정보
     * @return 수정된 사용자
     * @throws UserNotFoundException 사용자를 못찾을 경우
     */
    User updateUser(Long id, UserData source) throws UserNotFoundException;

    /**
     * 사용자를 삭제합니다.
     * @param id 삭제할 사용자 id
     */
    void deleteUser(Long id);

    /**
     * 이메일 중복이 되었다면 true, 그렇지 않다면 false를 리턴합니다.
     * @oaram 중복을 확인하려는 이메일 주소
     * @return 중복한 이메일이 존재한다면 true / 그렇지 않다면 false
     */
    boolean emailDuplicateCheck(String mail);

}

