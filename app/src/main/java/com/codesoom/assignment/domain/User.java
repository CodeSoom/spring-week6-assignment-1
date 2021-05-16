package com.codesoom.assignment.domain;

import com.codesoom.assignment.errors.AuthenticationFailException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue
    private Long id;

    private String email;

    private String name;

    private String password;

    @Builder.Default
    private boolean deleted = false;

    public void changeWith(User source) {
        name = source.name;
        password = source.password;
    }

    public void destroy() {
        deleted = true;
    }

    /**
     * 주어진 비밀번호로 실제 사용자의 비밀번호와 비교를 합니다.
     * 삭제된 사용자이거나 비밀번호가 일치하지 않을 경우 예외를 던집니다.
     * @param password 비밀번호
     * @return true 인증 성공 확인
     * @throws AuthenticationFailException 인증 실패 예외
     */
    public boolean authenticate(String password) {
        if (deleted) {
            throw new AuthenticationFailException("존재하지 않는 사용자 입니다");
        }

        if (!password.equals(this.password)) {
            throw new AuthenticationFailException("아이디 또는 혹은 비밀번호가 일치하지 않습니다");
        }
        
        return true;
    }
}
