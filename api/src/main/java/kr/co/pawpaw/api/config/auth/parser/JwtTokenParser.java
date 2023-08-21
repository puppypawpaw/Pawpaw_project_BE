package kr.co.pawpaw.api.config.auth.parser;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import kr.co.pawpaw.api.config.auth.provider.JwtTokenProvider;
import kr.co.pawpaw.api.config.property.JwtProperties;
import kr.co.pawpaw.domainredis.auth.domain.TokenType;
import kr.co.pawpaw.domainredis.auth.service.query.RefreshTokenQuery;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtTokenParser {
    private final RefreshTokenQuery refreshTokenQuery;
    private final JwtParser jwtParser;

    public JwtTokenParser(
        final JwtProperties jwtProperties,
        final RefreshTokenQuery refreshTokenQuery
    ) {
        this.refreshTokenQuery = refreshTokenQuery;
        this.jwtParser = Jwts.parserBuilder().setSigningKey(jwtProperties.getSecretKey()).build();
    }

    public Authentication extractAuthentication(final String token) {
        try {
            Claims claims = jwtParser.parseClaimsJws(token).getBody();
            Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(JwtTokenProvider.AUTHORITIES_KEY).toString().split(JwtTokenProvider.AUTHORITIES_DELIMITER))
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());

            UserDetails principal = User.builder()
                .username(claims.getSubject())
                .password("N/A")
                .authorities(authorities)
                .build();
            return new UsernamePasswordAuthenticationToken(principal, token, authorities);
        } catch (JwtException | IllegalArgumentException | NullPointerException exception) {
            throw new BadCredentialsException(exception.getMessage());
        }
    }

    public boolean validateAccessToken(final String token) {
        return validateToken(token, TokenType.ACCESS);
    }

    public boolean validateRefreshToken(final String token) {
        return refreshTokenQuery.existsByValue(token) && validateToken(token, TokenType.REFRESH);
    }

    private boolean validateToken(
        final String token,
        final TokenType tokenType
    ) {
        try {
            Claims claims = jwtParser.parseClaimsJws(token).getBody();
            TokenType extractedType = TokenType.valueOf((String)claims.get(JwtTokenProvider.TOKEN_TYPE_KEY));
            return !claims.getExpiration().before(new Date()) && extractedType.equals(tokenType);
        } catch (JwtException | IllegalArgumentException exception) {
            return false;
        }
    }
}
