package com.codesoom.assignment.utils;

import com.codesoom.assignment.errors.InvalidPasswordException;

/**
 * 비밀번호 유효성 검사기
 */
public class PasswordValidator {

    private PasswordValidator() {
    }

    /**
     * 비밀번호가 연속되는 숫자를 포함한다면 예외를 던집니다.
     *
     * @param password  비밀번호
     * @param standardLength 기준이 되는 연속되는 숫자 길이
     *
     * @throws InvalidPasswordException  연속되는 숫자를 포함한 경우
     *
     */
    public static void isConsecutiveNumber(String password, int standardLength) {

        int consecutiveNumberCount = 0;
        char beforeCharacter = 0;

        for (char character : password.toCharArray()) {
            if(character < 48 || character > 57) {
                consecutiveNumberCount = 0;
                continue;
            }

            if(consecutiveNumberCount == 0) {
                consecutiveNumberCount = 1;
            } else if(character == (beforeCharacter + 1)) {
                consecutiveNumberCount = consecutiveNumberCount + 1;
            }

            if(consecutiveNumberCount == standardLength) {
                throw new InvalidPasswordException(
                        String.format("비밀번호 %s는 연속되는 %d개의 숫자를 가지고 있습니다", password, standardLength)
                );
            }
            beforeCharacter = character;
        }
    }
}
