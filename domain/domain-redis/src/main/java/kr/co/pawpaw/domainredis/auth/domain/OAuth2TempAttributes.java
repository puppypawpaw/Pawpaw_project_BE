package kr.co.pawpaw.domainredis.auth.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@RedisHash(value = "OAuth2TempAttributes", timeToLive = 60 * 60 * 24L)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OAuth2TempAttributes {
    @Id
    private String key;
    private String name;
    private String email;
    private String profileImageUrl;
    private String provider;

    @Builder
    public OAuth2TempAttributes(
        final String key,
        final String name,
        final String email,
        final String profileImageUrl,
        final String provider
    ) {
        this.key = key;
        this.name = name;
        this.email = email;
        this.profileImageUrl = profileImageUrl;
        this.provider = provider;
    }
}
