package kr.co.pawpaw.domainredis.auth.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@Getter
@RedisHash(value = "verifiedPhoneNumber")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class VerifiedPhoneNumber {
    @Id
    private String id;
    @TimeToLive
    private Long ttl;

    @Builder
    public VerifiedPhoneNumber(
        final String phoneNumber,
        final String usagePurpose
    ) {
        this.id = getCompositeKey(phoneNumber, usagePurpose);
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
