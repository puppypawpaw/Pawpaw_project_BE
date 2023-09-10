package kr.co.pawpaw.domainredis.auth.repository;

import kr.co.pawpaw.domainredis.auth.domain.VerificationCode;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface VerificationCodeRepository extends CrudRepository<VerificationCode, String> {
    Optional<VerificationCode> findByIdAndCode(final String id, final String code);
}
