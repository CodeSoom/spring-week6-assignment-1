package com.codesoom.assignment.global.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Calendar;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;


class TimeUtilTest {
    private Date dateTime;

    @BeforeEach
    void setUp() {
        Calendar cal = Calendar.getInstance();
        cal.set(2022, Calendar.JANUARY, 1, 0, 0, 0);
        cal.set(Calendar.MILLISECOND, 0);
        dateTime = cal.getTime();
    }

    @DisplayName("LocalDatetime을 Date로 변경한 값이 같은지 비교합니다")
    @Test
    void convertLocalDatetime() {
        LocalDateTime localDateTime = LocalDateTime.of(2022, Month.JANUARY, 1, 0, 0);

        assertThat(TimeUtil.convertLocalDateTime(localDateTime)).hasSameTimeAs(dateTime);
    }
}
