package com.puppy.pawpaw_project_be.config.auth.filter;

import com.puppy.pawpaw_project_be.config.auth.application.TokenService;
import com.puppy.pawpaw_project_be.config.auth.parser.JwtTokenParser;
import com.puppy.pawpaw_project_be.domain.auth.domain.TokenType;
import com.puppy.pawpaw_project_be.domain.user.domain.User;
import com.puppy.pawpaw_project_be.domain.user.domain.UserId;
import com.puppy.pawpaw_project_be.domain.user.domain.repository.UserRepository;
import com.puppy.pawpaw_project_be.exception.user.NotFoundUserException;
import com.puppy.pawpaw_project_be.util.CookieUtil;
import org.springframework.beans.factory.annotation.Value;
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
public class JwtFilter extends OncePerRequestFilter {
	private final JwtTokenParser jwtTokenParser;
	private final UserRepository userRepository;
	private final TokenService tokenService;
	private final Long accessTokenLifeTime;
	private final String domain;
	private final String sameSite;

	public JwtFilter(
		final JwtTokenParser jwtTokenParser,
		final UserRepository userRepository,
		final TokenService tokenService,
		@Value("${custom.jwt.access-token-life-time}") final Long accessTokenLifeTime,
		@Value("${custom.cookieDomain}") final String domain,
		@Value("${custom.sameSite}") final String sameSite
	) {
		this.jwtTokenParser = jwtTokenParser;
		this.userRepository = userRepository;
		this.tokenService = tokenService;
		this.accessTokenLifeTime = accessTokenLifeTime;
		this.domain = domain;
		this.sameSite = sameSite;
	}

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
			String reissueAccessToken = refreshTokenLogin(tokenService.refreshTokenByUserId(refreshToken));

			Authentication authentication = jwtTokenParser.extractAuthentication(reissueAccessToken);
			SecurityContextHolder.getContext().setAuthentication(authentication);

			CookieUtil.addCookie(response, TokenType.ACCESS.name(), reissueAccessToken, accessTokenLifeTime / 1000, domain, sameSite);
		} else {
			CookieUtil.deleteCookie(request, response, TokenType.ACCESS.name(), domain, sameSite);
			CookieUtil.deleteCookie(request, response, TokenType.REFRESH.name(), domain, sameSite);
		}

		filterChain.doFilter(request, response);
	}

	private  String refreshTokenLogin(final UserId userId) {
		User user = userRepository.findById(userId)
			.orElseThrow(NotFoundUserException::new);
		UserDetails userDetails = builder()
			.username(user.getUserId().getValue())
			.password(user.getPassword())
			.authorities(Collections.singleton(new SimpleGrantedAuthority(user.getRole().name())))
			.build();
		Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
		return tokenService.createAccessToken(authentication);
	}

	public static String resolveToken(final Cookie token) {
		return CookieUtil.deserialize(token, String.class);
	}
}
