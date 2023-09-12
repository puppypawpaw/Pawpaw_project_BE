package kr.co.pawpaw.api.util.time;

import lombok.experimental.UtilityClass;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@UtilityClass
public class TimeUtil {
    public String getYearMonthDay(final LocalDateTime ldt) {
        return ldt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    public String getTimeBeforeString(final LocalDateTime ldt) {
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
