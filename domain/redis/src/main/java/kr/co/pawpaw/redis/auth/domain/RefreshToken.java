package kr.co.pawpaw.redis.auth.domain;

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
    private Long ttl;

    @Builder
    public RefreshToken(
        final String userId,
        final String value
    ) {
        this.userId = userId;
        this.value = value;
    }

    public void updateTtl(final Long ttl) {
        this.ttl = ttl;
    }
}
