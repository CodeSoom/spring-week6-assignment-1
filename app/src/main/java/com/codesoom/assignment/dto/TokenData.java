package com.codesoom.assignment.dto;

import com.github.dozermapper.core.Mapping;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TokenData {
    @Mapping("token")
    private String token;

    @Mapping("createTokenDate")
    private LocalDateTime createTokenDate;
}
