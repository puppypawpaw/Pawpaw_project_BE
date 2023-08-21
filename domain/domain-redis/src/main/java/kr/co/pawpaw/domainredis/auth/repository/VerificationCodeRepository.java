package kr.co.pawpaw.domainredis.auth.repository;

import kr.co.pawpaw.domainredis.auth.domain.VerificationCode;
import org.springframework.data.repository.CrudRepository;

public interface VerificationCodeRepository extends CrudRepository<VerificationCode, String> {
    boolean existsByIdAndCode(final String id, final String code);
}
