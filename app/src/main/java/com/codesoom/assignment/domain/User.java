package com.codesoom.assignment.domain;

import com.codesoom.assignment.errors.UserAuthenticationFailException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
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

    @Column(unique = true)
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

    public boolean authenticate(String password) {
        if (isDeleted()) {
            throw new UserAuthenticationFailException("이미 삭제된 회원입니다.");
        }

        if (!password.equals(this.password)) {
            throw new UserAuthenticationFailException("비밀번호가 일치하지 않습니다.");
        }

        return true;
    }

}
