package com.codesoom.assignment.application.users;

import com.codesoom.assignment.domain.users.User;

/**
 * 회원 정보 수정을 담당합니다.
 */
public interface UserUpdateService {

    /**
     * 식별자에 해당하는 회원 정보를 수정합니다.
     *
     * @param id 회원 식별자
     * @param userSaveRequest 사용자가 입력한 회원 정보 수정 데이터
     * @throws UserNotFoundException 식별자로 회원을 찾지 못한 경우
     */
    User updateUser(Long id, UserSaveRequest userSaveRequest);

}
