package kr.co.pawpaw.redis.auth.service.command;

import kr.co.pawpaw.redis.auth.domain.RefreshToken;
import kr.co.pawpaw.redis.auth.repository.RefreshTokenRepository;
import kr.co.pawpaw.redis.config.property.TtlProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefreshTokenCommand {
    private final RefreshTokenRepository refreshTokenRepository;
    private final TtlProperties ttlProperties;

    public RefreshToken save(final RefreshToken refreshToken) {
        refreshToken.updateTtl(ttlProperties.getRefreshToken());
        return refreshTokenRepository.save(refreshToken);
    }

    public void deleteById(final String userId) {
        refreshTokenRepository.deleteById(userId);
    }
}
