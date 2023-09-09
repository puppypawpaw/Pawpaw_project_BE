package kr.co.pawpaw.api.util.time;

import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@UtilityClass
public class TimeUtil {
    public String getYearMonthDay(final LocalDateTime ldt) {
        return ldt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
}
