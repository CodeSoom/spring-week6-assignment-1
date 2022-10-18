package com.codesoom.assignment.common.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    COMMON_INVALID_PARAMETER("요청한 값이 올바르지 않습니다.", HttpStatus.BAD_REQUEST),
    INVALID_TOKEN("잘못된 토큰값입니다.", HttpStatus.UNAUTHORIZED),
    INVALID_EMAIL("등록되지 않은 이메일입니다.", HttpStatus.NOT_FOUND),
    INVALID_PASSWORD("잘못된 패스워드입니다.", HttpStatus.BAD_REQUEST),

    COMMON_NOT_FOUND("등록되지 않은 데이터입니다.", HttpStatus.NOT_FOUND),
    USER_NOT_FOUND("등록되지 않은 회원입니다.", HttpStatus.NOT_FOUND),
    PRODUCT_NOT_FOUND("요청하신 상품이 없습니다.", HttpStatus.NOT_FOUND);

    private final String errorMsg;
    private final HttpStatus httpStatus;
}
