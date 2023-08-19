package kr.co.pawpaw.domainredis.auth.service.command;

import kr.co.pawpaw.domainredis.auth.domain.RefreshToken;
import kr.co.pawpaw.domainredis.auth.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefreshTokenCommand {
    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshToken save(final RefreshToken refreshToken) {
        return refreshTokenRepository.save(refreshToken);
    }

    public void deleteById(final String userId) {
        refreshTokenRepository.deleteById(userId);
    }
}
