package kr.co.pawpaw.api.config.auth.application;

import kr.co.pawpaw.api.config.auth.provider.JwtTokenProvider;
import kr.co.pawpaw.common.exception.user.NotSignedInException;
import kr.co.pawpaw.domainrdb.user.domain.UserId;
import kr.co.pawpaw.domainredis.auth.domain.RefreshToken;
import kr.co.pawpaw.domainredis.auth.domain.repository.RefreshTokenRepository;
import kr.co.pawpaw.domainredis.auth.dto.response.TokenResponse;
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
            .map(RefreshToken::getUserId)
            .orElseThrow(IllegalArgumentException::new));
    }
}
