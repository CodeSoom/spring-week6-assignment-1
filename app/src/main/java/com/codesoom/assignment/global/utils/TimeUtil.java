package com.codesoom.assignment.global.utils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * 시간 처리를 담당합니다.
 */
public class TimeUtil {
    private TimeUtil() {
    }

    /**
     * LocalDatetime 값을 Date로 변환해 리턴합니다.
     *
     * @param localDateTime 입력받은 시간
     * @return 포맷이 변경된 시간
     */
    public static Date convertLocalDateTime(LocalDateTime localDateTime) {
        ZonedDateTime zonedDateTime = ZonedDateTime.of(localDateTime, ZoneId.systemDefault());
        GregorianCalendar cal = GregorianCalendar.from(zonedDateTime);
        return cal.getTime();
    }
}
