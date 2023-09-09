package kr.co.pawpaw.domainredis.auth.repository;

import kr.co.pawpaw.domainredis.auth.domain.ChangePasswordTempKey;
import org.springframework.data.repository.CrudRepository;

public interface ChangePasswordTempKeyRepository extends CrudRepository<ChangePasswordTempKey, String> {
}
