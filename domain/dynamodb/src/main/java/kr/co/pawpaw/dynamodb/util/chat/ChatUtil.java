package kr.co.pawpaw.dynamodb.util.chat;

import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@UtilityClass
public class ChatUtil {
    private static final DateTimeFormatter defaultDateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final long CUSTOM_EPOCH = 1690000000000L;

    public Long createChatSortId() {
        long ts = LocalDateTime.now().toInstant(ZoneOffset.ofHours(9)).toEpochMilli() - CUSTOM_EPOCH;
        long randId = (long) Math.floor(Math.random() * 512);
        ts <<= 6;
        return (ts * 512) + randId;
    }

    public String localDateTimeToDefaultTimeString(final LocalDateTime ldt) {
        if (Objects.isNull(ldt)) {
            return null;
        }

        return ldt.format(defaultDateTimeFormatter);
    }

    public String getJoinDataFromNickname(final String nickname) {
        return String.format("%s님이 참가하셨습니다.", nickname);
    }

    public String getLeaveDataFromNickname(final String nickname) {
        return String.format("%s님이 퇴장하셨습니다.", nickname);
    }

}
