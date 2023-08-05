package com.puppy.pawpaw_project_be.config.auth.application;

import com.puppy.pawpaw_project_be.config.auth.provider.JwtTokenProvider;
import com.puppy.pawpaw_project_be.domain.auth.domain.RefreshToken;
import com.puppy.pawpaw_project_be.domain.auth.domain.repository.RefreshTokenRepository;
import com.puppy.pawpaw_project_be.domain.user.domain.UserId;
import com.puppy.pawpaw_project_be.domain.auth.dto.response.TokenResponse;
import com.puppy.pawpaw_project_be.exception.user.NotSignedInException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class TokenService {
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final Long accessTokenLifeTime;
    private final Long refreshTokenLifeTime;

    public TokenService(
        final JwtTokenProvider jwtTokenProvider,
        final RefreshTokenRepository refreshTokenRepository,
        @Value("${custom.jwt.access-token-life-time}") final Long accessTokenLifeTime,
        @Value("${custom.jwt.refresh-token-life-time}") final Long refreshTokenLifeTime
    ) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.refreshTokenRepository = refreshTokenRepository;
        this.accessTokenLifeTime = accessTokenLifeTime;
        this.refreshTokenLifeTime = refreshTokenLifeTime;
    }

    public TokenResponse createTokenResponse(final Authentication authentication) {
        String accessToken = jwtTokenProvider.createAccessToken(authentication);
        String refreshToken = jwtTokenProvider.createRefreshToken();

        return TokenResponse.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .accessTokenLifeTime(accessTokenLifeTime)
            .refreshTokenLifeTime(refreshTokenLifeTime)
            .build();
    }

    public String createAccessToken(Authentication authenticate) {
        return jwtTokenProvider.createAccessToken(authenticate);
    }

    public void saveRefreshToken(
        final UserId userId,
        final String refreshToken
    ) {
        refreshTokenRepository.save(
            RefreshToken.builder()
                .userId(userId.getValue())
                .value(refreshToken)
                .timeout(refreshTokenLifeTime)
                .build());
    }

    public void deleteRefreshToken(
        final String userId
    ) {
        if (Objects.isNull(userId)) {
            throw new NotSignedInException();
        }

        refreshTokenRepository.deleteById(userId);
    }

    public UserId refreshTokenByUserId(final String refreshToken) {
        return UserId.of(refreshTokenRepository.findByValue(refreshToken)
            .orElseThrow(IllegalArgumentException::new)
            .getUserId());
    }
}
