package com.codesoom.assignment.domain;

import lombok.Builder;
import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Builder
@Getter
public class TokenManager {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    private LocalDateTime createTokenDate;

    private TokenManager(Long id, String token, LocalDateTime createTokenDate) {
        this.id = id;
        this.token = token;
        this.createTokenDate = createTokenDate;
    }

    protected TokenManager() {
    }

    public void changeWithExpiredLength(LocalDateTime createTokenDate) {
        this.createTokenDate = createTokenDate;
    }

}


