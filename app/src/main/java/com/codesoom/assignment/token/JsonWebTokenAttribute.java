package com.codesoom.assignment.token;

/**
 * JWT 생성시 필요 데이터
 */
public interface JsonWebTokenAttribute {

    String getId();

    Integer getExpiredMinute();

    static JsonWebTokenAttribute of(Long id) {
        return new JsonWebTokenAttribute() {
            @Override
            public String getId() {
                return String.valueOf(id);
            }

            @Override
            public Integer getExpiredMinute() {
                return null;
            }
        };
    }
}
