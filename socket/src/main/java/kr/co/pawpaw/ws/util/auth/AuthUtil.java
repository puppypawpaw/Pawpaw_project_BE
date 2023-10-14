package kr.co.pawpaw.ws.util.auth;

import kr.co.pawpaw.mysql.user.domain.UserId;
import lombok.experimental.UtilityClass;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;

import java.util.Objects;

@UtilityClass
public class AuthUtil {
    public UserId getUserIdFromSimpMessageHeaderAccessor(final SimpMessageHeaderAccessor accessor) {
        return UserId.of((String) Objects.requireNonNull(accessor.getSessionAttributes()).get("userId"));
    }
}
