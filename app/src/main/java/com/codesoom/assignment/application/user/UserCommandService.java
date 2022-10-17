package com.codesoom.assignment.application.user;

import com.codesoom.assignment.domain.user.User;

public interface UserCommandService {

    /**
     * 신규 회원정보를 등록하고 등록된 회원정보를 리턴한다.
     * @param command 신규 회원정보
     * @return 등록된 회원정보
     */
    User createUser(UserCommand.Register command);

    /**
     * 회원정보를 수정하고 수정된 회원정보를 리턴한다.
     * @param command 수정 회원정보
     * @return 수정된 상품
     */
    User updateUser(UserCommand.UpdateRequest command);

    /**
     * 회원 ID의 회원정보를 삭제한다.
     * @param id 삭제할 회원 ID
     */
    void deleteUser(Long id);

}
