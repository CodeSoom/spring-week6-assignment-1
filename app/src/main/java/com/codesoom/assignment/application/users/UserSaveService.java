package com.codesoom.assignment.application.users;

import com.codesoom.assignment.domain.users.User;

/**
 * 회원 생성을 담당합니다.
 */
public interface UserSaveService {

    /**
     * 요청받은 정보로 회원을 생성합니다.
     *
     * @param userSaveRequest 사용자가 입력한 회원 정보
     * @return 저장된 회원 정보
     */
    User saveUser(UserSaveRequest userSaveRequest);

}
