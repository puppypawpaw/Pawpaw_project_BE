package kr.co.pawpaw.api.config.property;

import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.security.Key;

@Getter
@Validated
@ConstructorBinding
@ConfigurationProperties(prefix = "custom.jwt")
public class JwtProperties {
    private final long accessTokenLifeTime;
    private final long refreshTokenLifeTime;
    @NotNull
    private final Key secretKey;

    public JwtProperties(
        final long accessTokenLifeTime,
        final long refreshTokenLifeTime,
        final String secretKey
    ) {
        this.accessTokenLifeTime = accessTokenLifeTime;
        this.refreshTokenLifeTime = refreshTokenLifeTime;
        this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes());
    }
}
