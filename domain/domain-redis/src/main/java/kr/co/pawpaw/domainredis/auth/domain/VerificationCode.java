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
@RedisHash(value = "verificationCode")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class VerificationCode {
    @Id
    @Indexed
    private String id;
    @Indexed
    private String code;
    @TimeToLive
    private Long ttl;

    @Builder
    public VerificationCode(
        final String phoneNumber,
        final String usagePurpose,
        final String code
    ) {
        this.id = getCompositeKey(phoneNumber, usagePurpose);
        this.code = code;
    }

    public static String getCompositeKey(final String phoneNumber, final String usagePurpose) {
        return phoneNumber + ":" + usagePurpose;
    }

    public String getPhoneNumber() {
        return this.id.split(":")[0];
    }

    public String getUsagePurpose() {
        return this.id.split(":")[1];
    }

    public void updateTtl(final Long ttl) {
        this.ttl = ttl;
    }
}
