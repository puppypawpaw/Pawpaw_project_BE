package kr.co.pawpaw.redis.auth.service.command;

import kr.co.pawpaw.redis.auth.domain.OAuth2TempAttributes;
import kr.co.pawpaw.redis.auth.repository.OAuth2TempAttributesRepository;
import kr.co.pawpaw.redis.config.property.TtlProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OAuth2TempAttributesCommand {
    private final OAuth2TempAttributesRepository oAuth2TempAttributesRepository;
    private final TtlProperties ttlProperties;

    public OAuth2TempAttributes save(final OAuth2TempAttributes entity) {
        entity.updateTtl(ttlProperties.getOauth2TempAttributes());
        return oAuth2TempAttributesRepository.save(entity);
    }

    public void deleteById(final String id) {
        oAuth2TempAttributesRepository.deleteById(id);
    }
}
