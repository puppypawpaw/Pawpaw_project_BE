package kr.co.pawpaw.redis.auth.repository;

import kr.co.pawpaw.redis.auth.domain.VerificationCode;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface VerificationCodeRepository extends CrudRepository<VerificationCode, String> {
    Optional<VerificationCode> findByIdAndCode(final String id, final String code);
}
