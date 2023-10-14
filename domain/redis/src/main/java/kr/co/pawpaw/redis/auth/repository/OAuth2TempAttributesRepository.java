package kr.co.pawpaw.redis.auth.repository;

import kr.co.pawpaw.redis.auth.domain.OAuth2TempAttributes;
import org.springframework.data.repository.CrudRepository;

public interface OAuth2TempAttributesRepository extends CrudRepository<OAuth2TempAttributes, String> {
}
