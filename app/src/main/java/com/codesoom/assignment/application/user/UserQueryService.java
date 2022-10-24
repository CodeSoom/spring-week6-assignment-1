package com.codesoom.assignment.application.user;

import com.codesoom.assignment.domain.user.User;

import java.util.List;

public interface UserQueryService {

    /**
     * 전체 회원정보를 리턴한다.
     * @return 전체 회원정보
     */
    List<User> getUsers();

    /**
     * 회워 ID의 회원정보를 리턴한다.
     * @param id 회원 ID
     * @return 검색된 회원정보
     */
    User getUser(Long id);

}
