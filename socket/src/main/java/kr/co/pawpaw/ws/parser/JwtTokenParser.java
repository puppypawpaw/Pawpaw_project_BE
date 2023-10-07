package kr.co.pawpaw.ws.parser;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import kr.co.pawpaw.redis.auth.domain.TokenType;
import kr.co.pawpaw.ws.property.JwtProperties;
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
    private final JwtParser jwtParser;
    public static final String AUTHORITIES_KEY = "auth";
    public static final String AUTHORITIES_DELIMITER = "<?>";
    public static final String TOKEN_TYPE_KEY = "type";

    public JwtTokenParser(
        final JwtProperties jwtProperties
    ) {
        this.jwtParser = Jwts.parserBuilder().setSigningKey(jwtProperties.getSecretKey()).build();
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
