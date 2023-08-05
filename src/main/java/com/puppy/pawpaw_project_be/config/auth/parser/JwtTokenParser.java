package com.puppy.pawpaw_project_be.config.auth.parser;

import com.puppy.pawpaw_project_be.domain.auth.domain.TokenType;
import com.puppy.pawpaw_project_be.domain.auth.domain.repository.RefreshTokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

import static com.puppy.pawpaw_project_be.config.auth.provider.JwtTokenProvider.*;

@Component
public class JwtTokenParser {
    private final JwtParser jwtParser;
    private final RefreshTokenRepository refreshTokenRepository;

    public JwtTokenParser(
        @Value("${custom.jwt.secret-key}") final String secretKey,
        final RefreshTokenRepository refreshTokenRepository
    ) {
        this.refreshTokenRepository = refreshTokenRepository;
        Key key = Keys.hmacShaKeyFor(secretKey.getBytes());
        this.jwtParser = Jwts.parserBuilder().setSigningKey(key).build();
    }

    public Authentication extractAuthentication(final String token) {
        try {
            Claims claims = jwtParser.parseClaimsJws(token).getBody();
            Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(AUTHORITIES_DELIMITER))
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
        return refreshTokenRepository.existsByValue(token) && validateToken(token, TokenType.REFRESH);
    }

    private boolean validateToken(
        final String token,
        final TokenType tokenType
    ) {
        try {
            Claims claims = jwtParser.parseClaimsJws(token).getBody();
            TokenType extractedType = TokenType.valueOf((String)claims.get(TOKEN_TYPE_KEY));
            return !claims.getExpiration().before(new Date()) && extractedType.equals(tokenType);
        } catch (JwtException | IllegalArgumentException exception) {
            return false;
        }
    }
}
