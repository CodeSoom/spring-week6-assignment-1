package com.codesoom.assignment;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AppTest {
    @Test
    @DisplayName("Application context load 정상 작동을 확인한다")
    void runApplication() {
        App.main(new String[] {});
    }
}
