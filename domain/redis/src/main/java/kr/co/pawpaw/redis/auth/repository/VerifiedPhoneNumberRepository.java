package kr.co.pawpaw.redis.auth.repository;

import kr.co.pawpaw.redis.auth.domain.VerifiedPhoneNumber;
import org.springframework.data.repository.CrudRepository;

public interface VerifiedPhoneNumberRepository extends CrudRepository<VerifiedPhoneNumber, String> {
}
