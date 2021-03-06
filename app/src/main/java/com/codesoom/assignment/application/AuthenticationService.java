package com.codesoom.assignment.application;

import com.codesoom.assignment.utils.JwtUtil;
import org.springframework.stereotype.Service;

/**
 * 인증 처리를 담당합니다.
 */
@Service
public class AuthenticationService {
   private JwtUtil jwtUtil;


   public AuthenticationService(JwtUtil jwtUtil) {
       this.jwtUtil = jwtUtil;
   }

    /**
     * userId에 따른 엑세스토큰을 반환합니다.
     * @return 엑세스 토큰
     */
    public String login() {
        return jwtUtil.encode(1L);
    }
}
