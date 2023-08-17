package kr.co.pawpaw.domainredis.auth.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

@Getter
@RedisHash(value = "refreshToken")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken {
    @Id
    private String userId;

    @Indexed
    private String value;

    @TimeToLive
    private Long timeout;

    @Builder
    public RefreshToken(
        final String userId,
        final String value,
        final Long timeout
    ) {
        this.userId = userId;
        this.value = value;
        this.timeout = timeout;
    }
}
