package kr.co.pawpaw.redis.auth.repository;

import kr.co.pawpaw.redis.auth.domain.RefreshToken;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {
    Optional<RefreshToken> findByValue(final String value);
    boolean existsByValue(final String value);
}
