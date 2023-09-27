package kr.co.pawpaw.api.config.auth.provider;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import kr.co.pawpaw.api.config.property.JwtProperties;
import kr.co.pawpaw.domainredis.auth.domain.TokenType;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {
    public static final String AUTHORITIES_KEY = "auth";
    public static final String AUTHORITIES_DELIMITER = "<?>";
    public static final String TOKEN_TYPE_KEY = "type";
    private final JwtProperties jwtProperties;


    private static String toString(final Collection<? extends GrantedAuthority> grantedAuthorities) {
        return grantedAuthorities.stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.joining(AUTHORITIES_DELIMITER));
    }

    public String createAccessToken(final Authentication authentication) {
        Date now = new Date();
        Date expiredAt = new Date(now.getTime() + jwtProperties.getAccessTokenLifeTime() * 1000);

        return Jwts.builder()
            .setSubject(authentication.getName())
            .claim(AUTHORITIES_KEY, toString(authentication.getAuthorities()))
            .claim(TOKEN_TYPE_KEY, TokenType.ACCESS)
            .signWith(jwtProperties.getSecretKey(), SignatureAlgorithm.HS512)
            .setIssuedAt(now)
            .setExpiration(expiredAt)
            .compact();
    }

    public String createRefreshToken() {
        Date now = new Date();
        Date expiredAt = new Date(now.getTime() + jwtProperties.getRefreshTokenLifeTime() * 1000);

        return Jwts.builder()
            .claim(TOKEN_TYPE_KEY, TokenType.REFRESH)
            .signWith(jwtProperties.getSecretKey(), SignatureAlgorithm.HS512)
            .setIssuedAt(now)
            .setExpiration(expiredAt)
            .compact();
    }
}
