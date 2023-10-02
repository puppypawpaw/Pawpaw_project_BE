package kr.co.pawpaw.redis.auth.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.util.UUID;

@Getter
@RedisHash(value = "oAuth2TempAttributes")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OAuth2TempAttributes {
    @Id
    private String key;
    private String name;
    private String email;
    private String profileImageUrl;
    private String provider;
    @TimeToLive
    private Long ttl;

    @Builder
    public OAuth2TempAttributes(
        final String name,
        final String email,
        final String profileImageUrl,
        final String provider
    ) {
        this.key = UUID.randomUUID().toString();
        this.name = name;
        this.email = email;
        this.profileImageUrl = profileImageUrl;
        this.provider = provider;
    }

    public void updateTtl(final Long ttl) {
        this.ttl = ttl;
    }
}
