package kr.co.pawpaw.redis.auth.service.query;

import kr.co.pawpaw.redis.auth.domain.RefreshToken;
import kr.co.pawpaw.redis.auth.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RefreshTokenQuery {
    private final RefreshTokenRepository refreshTokenRepository;

    public Optional<RefreshToken> findByValue(final String value) {
        return refreshTokenRepository.findByValue(value);
    }

    public boolean existsByValue(final String value) {
        return refreshTokenRepository.existsByValue(value);
    }
}
