package com.codesoom.assignment.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SequenceGenerator(
        name = "USER_SEQ_GENERATOR",
        sequenceName = "USER_SEQ", // 매핑할 데이터베이스 시퀀스 이름
        initialValue = 1, allocationSize = 1
)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String username;

    private String email;

    private String password;

    private User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public static User of(String username, String email, String password) {
        return new User(username, email, password);
    }

    public void change(
            String username,
            String email,
            String password
    ) {
        this.username = username;
        this.email = email;
        this.password = password;
    }


}
