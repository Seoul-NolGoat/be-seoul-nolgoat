package wad.seoul_nolgoat.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class DateTimeUtil {

    // LocalDateTime을 "yyyy-MM-dd" 형식의 문자열로 변환
    public static String formatDate(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return dateTime.format(formatter);
    }

    // 현재 시간과의 차이를 반환
    public static String timeAgo(LocalDateTime dateTime) {
        LocalDateTime now = LocalDateTime.now();

        long years = ChronoUnit.YEARS.between(dateTime, now);
        if (years >= 1) {
            return years + "년 전";
        }

        long months = ChronoUnit.MONTHS.between(dateTime, now);
        if (months >= 1) {
            return months + "달 전";
        }

        long days = ChronoUnit.DAYS.between(dateTime, now);
        if (days >= 1) {
            return days + "일 전";
        }

        long hours = ChronoUnit.HOURS.between(dateTime, now);
        if (hours >= 1) {
            return hours + "시간 전";
        }

        long minutes = ChronoUnit.MINUTES.between(dateTime, now);
        if (minutes >= 1) {
            return minutes + "분 전";
        }

        return "방금 전";
    }
}
