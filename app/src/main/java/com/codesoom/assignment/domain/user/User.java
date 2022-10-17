package com.codesoom.assignment.domain.user;

import com.codesoom.assignment.common.exception.InvalidParamException;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.util.StringUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@lombok.Generated
@Entity
@Getter
@ToString(of = {"id", "name", "password", "email"})
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue
    @Column(name = "user_id")
    private Long id;
    private String name;
    private String password;
    private String email;

    protected User() {
    }

    @Builder
    public User(Long id, String name, String password, String email) {
        if (StringUtils.isEmpty(name)){
            throw new InvalidParamException("이름이 비어있습니다.");
        }
        if (StringUtils.isEmpty(password)) {
            throw new InvalidParamException("비밀번호가 비어있습니다.");
        }

        this.id = id;
        this.name = name;
        this.password = password;
        this.email = email;
    }

    public void modifyUserInfo(User user) {
        this.name = user.getName();
        this.password = user.getPassword();
        if (!StringUtils.isEmpty(user.getEmail())) {
            this.email = user.getEmail();
        }
    }
}
