package com.codesoom.assignment;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("App 클래스")
class AppTest {

    @Test
    @DisplayName("main 메서드는 스프링 애플리케이션을 실행한다")
    void springApplicationContextTest() {
        String[] args = new String[]{
                "--spring.main.banner-mode=off",
                "--logging.level.root=ERROR"
        };
        App.main(args);
    }
}
