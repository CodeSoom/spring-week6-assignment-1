package com.codesoom.assignment.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * User Entity 클래스
 */
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

    /**
     * name, password를 source의 name, password로 바꾼다.
     *
     * @param source 수정할 정보가 들어있는 User 객체
     */
    public void changeWith(User source) {
        name = source.name;
        password = source.password;
    }

    /**
     * deleted를 true로 바꾼다.
     */
    public void destroy() {
        deleted = true;
    }

    /**
     * deleted가 false이고, 매개변수로 주어진 password가 password와 일치하면 true, 그렇지 않으면 false를 반환한다.
     * @param password 비밀번호
     */
    public boolean authenticate(String password) {
        return !deleted && password.equals(this.password);
    }
}
