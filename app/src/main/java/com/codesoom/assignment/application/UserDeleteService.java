package com.codesoom.assignment.application;

/**
 * 회원 정보 삭제를 담당합니다.
 */
public interface UserDeleteService {

    /**
     * 식별자에 해당하는 회원 정보를 삭제합니다.
     *
     * @param id 회원 식별자
     * @throws UserNotFoundException 식별자로 회원을 찾지 못한 경우
     */
    void deleteUser(Long id);

}
