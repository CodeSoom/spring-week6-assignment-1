package com.codesoom.assignment;

import com.github.dozermapper.core.Mapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DisplayName("App 클래스")
public final class AppTest {
    @Nested
    @DisplayName("dozerMapper 메서드는")
    public final class Describe_dozerMapper {
        @Test
        @DisplayName("Mapper를 리턴한다.")
        public void it_returns_mapper_object() {
            final App app = new App();
            assertThat(app.dozerMapper())
                .isInstanceOf(Mapper.class);
        }
    }

    @Nested
    @DisplayName("main 메서드는")
    public final class Describe_main {
        @Test
        @DisplayName("스프링 어플리케이션을 작동시킨다.")
        public void it_runs_spring_application() {
            App.main(new String[] {});
        }
    }
}
