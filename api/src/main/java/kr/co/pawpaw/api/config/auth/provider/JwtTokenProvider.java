package kr.co.pawpaw.api.config.auth.provider;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import kr.co.pawpaw.domainredis.auth.domain.TokenType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {
    public static final String AUTHORITIES_KEY = "auth";
    public static final String AUTHORITIES_DELIMITER = "<?>";
    public static final String TOKEN_TYPE_KEY = "type";
    private final Long accessTokenLifeTime;
    private final Long refreshTokenLifeTime;
    private final Key secretKey;


    public JwtTokenProvider(
        @Value("${custom.jwt.access-token-life-time}") final Long accessTokenLifeTime,
        @Value("${custom.jwt.refresh-token-life-time}") final Long refreshTokenLifeTime,
        @Value("${custom.jwt.secret-key}") final String secretKey
    ) {
        this.accessTokenLifeTime = accessTokenLifeTime;
        this.refreshTokenLifeTime = refreshTokenLifeTime;
        this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    private static String toString(final Collection<? extends GrantedAuthority> grantedAuthorities) {
        return grantedAuthorities.stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.joining(AUTHORITIES_DELIMITER));
    }

    public String createAccessToken(final Authentication authentication) {
        Date now = new Date();
        Date expiredAt = new Date(now.getTime() + accessTokenLifeTime);

        return Jwts.builder()
            .setSubject(authentication.getName())
            .claim(AUTHORITIES_KEY, toString(authentication.getAuthorities()))
            .claim(TOKEN_TYPE_KEY, TokenType.ACCESS)
            .signWith(secretKey, SignatureAlgorithm.HS512)
            .setIssuedAt(now)
            .setExpiration(expiredAt)
            .compact();
    }

    public String createRefreshToken() {
        Date now = new Date();
        Date expiredAt = new Date(now.getTime() + refreshTokenLifeTime);

        return Jwts.builder()
            .claim(TOKEN_TYPE_KEY, TokenType.REFRESH)
            .signWith(secretKey, SignatureAlgorithm.HS512)
            .setIssuedAt(now)
            .setExpiration(expiredAt)
            .compact();
    }
}
