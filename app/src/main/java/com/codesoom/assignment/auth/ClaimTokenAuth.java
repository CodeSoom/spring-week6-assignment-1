package com.codesoom.assignment.auth;

/**
 * Claim Token 기반 유효한 토큰을 생성하고, 전달 받은 토큰의 유효성을 검증한다
 * <p>
 * All Known Extending Classes:
 * @see JwtAuth
 * </p>
 */
public interface ClaimTokenAuth <T>{
    /**
     * 사용자 아이디가 담긴 Claim Token 반환한다
     * <p>
     * @param id 사용자 아이디
     * @return 문자열 타입의 Claim Token
     * </p>
     */
    String encode(Long id);

    /**
     * claim token 전달 받아서 의미 있는 정보를 반환한다
     * <p>
     * @param token 디코딩의 대상이 되는 Claim Token
     * @return 토큰으로부터 추출한 의미 있는 정보
     * </p>
     */
    T decode(String token);
}
