package kr.co.pawpaw.api.util.time;

import lombok.experimental.UtilityClass;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@UtilityClass
public class TimeUtil {
    private static final DateTimeFormatter defaultDateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public String getYearMonthDay(final LocalDateTime ldt) {
        if (Objects.isNull(ldt)) {
            return null;
        }

        return ldt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    public LocalDateTime defaultTimeStringToLocalDateTime(final String timeString) {
        return LocalDateTime.parse(timeString, defaultDateTimeFormatter);
    }

    public String localDateTimeToDefaultTimeString(final LocalDateTime ldt) {
        if (Objects.isNull(ldt)) {
            return null;
        }

        return ldt.format(defaultDateTimeFormatter);
    }

    public String getTimeBeforeString(final LocalDateTime ldt) {
        if (Objects.isNull(ldt)) {
            return null;
        }

        long beforeMinutes = Duration.between(ldt, LocalDateTime.now()).toMinutes();

        if (beforeMinutes == 0) {
            return "방금";
        } else if (beforeMinutes < 60) {
            return beforeMinutes + "분전";
        } else if (beforeMinutes < 60 * 24) {
            return beforeMinutes / 60 + "시간전";
        } else {
            return beforeMinutes / (60 * 24) + "일전";
        }
    }
}
