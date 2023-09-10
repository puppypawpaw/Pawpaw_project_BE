package kr.co.pawpaw.domainredis.auth.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.util.UUID;

@Getter
@RedisHash(value = "changePasswordTempKey")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChangePasswordTempKey {
    @Id
    private String key;
    private String userId;
    @TimeToLive
    private Long ttl;

    @Builder
    public ChangePasswordTempKey(
        final String userId
    ) {
        this.key = UUID.randomUUID().toString();
        this.userId = userId;
    }

    public void updateTtl(final Long ttl) {
        this.ttl = ttl;
    }
}
