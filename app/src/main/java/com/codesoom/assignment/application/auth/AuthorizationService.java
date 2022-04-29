package com.codesoom.assignment.application.auth;

/**
 * 회원 인가(허가)를 담당합니다.
 */
public interface AuthorizationService {

    /**
     * 사용자가 입력한 토큰을 검증한 뒤 토큰에 저장된 userId를 반환합니다.
     *
     * @param accessToken 사용자가 입력한 토큰
     * @return 토큰에 저장된 userId
     */
    Long parseToken(String accessToken);

}
