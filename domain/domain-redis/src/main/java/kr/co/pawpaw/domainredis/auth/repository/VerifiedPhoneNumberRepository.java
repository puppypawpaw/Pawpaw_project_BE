package kr.co.pawpaw.domainredis.auth.repository;

import kr.co.pawpaw.domainredis.auth.domain.VerifiedPhoneNumber;
import org.springframework.data.repository.CrudRepository;

public interface VerifiedPhoneNumberRepository extends CrudRepository<VerifiedPhoneNumber, String> {
}
