package kr.co.pawpaw.api.config.auth.filter;

import kr.co.pawpaw.api.config.auth.parser.JwtTokenParser;
import kr.co.pawpaw.api.config.auth.provider.JwtTokenProvider;
import kr.co.pawpaw.api.config.property.CookieProperties;
import kr.co.pawpaw.api.config.property.JwtProperties;
import kr.co.pawpaw.common.exception.user.NotFoundUserException;
import kr.co.pawpaw.api.util.cookie.CookieUtil;
import kr.co.pawpaw.mysql.user.domain.User;
import kr.co.pawpaw.mysql.user.domain.UserId;
import kr.co.pawpaw.mysql.user.service.query.UserQuery;
import kr.co.pawpaw.redis.auth.domain.RefreshToken;
import kr.co.pawpaw.redis.auth.domain.TokenType;
import kr.co.pawpaw.redis.auth.service.query.RefreshTokenQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

import static org.springframework.security.core.userdetails.User.builder;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
	private final JwtTokenParser jwtTokenParser;
	private final JwtTokenProvider jwtTokenProvider;
	private final UserQuery userQuery;
	private final RefreshTokenQuery refreshTokenQuery;
	private final CookieProperties cookieProperties;
	private final JwtProperties jwtProperties;

	@Override
	protected void doFilterInternal(
		final HttpServletRequest request,
		final HttpServletResponse response,
		final FilterChain filterChain
	) throws ServletException, IOException {
		String accessToken = CookieUtil.getCookie(request, TokenType.ACCESS.name())
			.map(JwtFilter::resolveToken)
			.orElse("");

		String refreshToken = CookieUtil.getCookie(request, TokenType.REFRESH.name())
			.map(JwtFilter::resolveToken)
			.orElse("");

		if (jwtTokenParser.validateAccessToken(accessToken)) {
			Authentication authentication = jwtTokenParser.extractAuthentication(accessToken);
			SecurityContextHolder.getContext().setAuthentication(authentication);
		} else if (jwtTokenParser.validateRefreshToken(refreshToken)) {
			UserId userId = findUserIdByRefreshToken(refreshToken);
			String reissueAccessToken = reissueAccessTokenByUserId(userId);

			Authentication authentication = jwtTokenParser.extractAuthentication(reissueAccessToken);
			SecurityContextHolder.getContext().setAuthentication(authentication);

			CookieUtil.addCookie(
				response,
				TokenType.ACCESS.name(),
				reissueAccessToken,
				jwtProperties.getAccessTokenLifeTime(),
				cookieProperties.getDomain(),
				cookieProperties.getSameSite());
		} else {
			CookieUtil.deleteCookie(request, response, TokenType.ACCESS.name());
			CookieUtil.deleteCookie(request, response, TokenType.REFRESH.name());
		}

		filterChain.doFilter(request, response);
	}

	private UserId findUserIdByRefreshToken(String refreshToken) {
		return UserId.of(refreshTokenQuery.findByValue(refreshToken)
			.map(RefreshToken::getUserId)
			.orElseThrow(IllegalArgumentException::new));
	}

	private  String reissueAccessTokenByUserId(final UserId userId) {
		User user = userQuery.findByUserId(userId)
			.orElseThrow(NotFoundUserException::new);
		UserDetails userDetails = builder()
			.username(user.getUserId().getValue())
			.password(user.getPassword())
			.authorities(Collections.singleton(new SimpleGrantedAuthority(user.getRole().name())))
			.build();
		Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

		return jwtTokenProvider.createAccessToken(authentication);
	}

	public static String resolveToken(final Cookie token) {
		return CookieUtil.deserialize(token, String.class);
	}
}
