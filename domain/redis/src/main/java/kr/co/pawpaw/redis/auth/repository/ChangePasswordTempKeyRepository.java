package kr.co.pawpaw.redis.auth.repository;

import kr.co.pawpaw.redis.auth.domain.ChangePasswordTempKey;
import org.springframework.data.repository.CrudRepository;

public interface ChangePasswordTempKeyRepository extends CrudRepository<ChangePasswordTempKey, String> {
}
