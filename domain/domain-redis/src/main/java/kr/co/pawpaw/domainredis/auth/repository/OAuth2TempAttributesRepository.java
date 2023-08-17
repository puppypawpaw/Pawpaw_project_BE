package kr.co.pawpaw.domainredis.auth.repository;

import kr.co.pawpaw.domainredis.auth.domain.OAuth2TempAttributes;
import org.springframework.data.repository.CrudRepository;

public interface OAuth2TempAttributesRepository extends CrudRepository<OAuth2TempAttributes, String> {
}
