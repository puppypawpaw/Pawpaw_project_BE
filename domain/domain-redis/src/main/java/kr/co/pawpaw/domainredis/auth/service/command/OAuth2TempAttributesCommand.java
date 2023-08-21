package kr.co.pawpaw.domainredis.auth.service.command;

import kr.co.pawpaw.domainredis.auth.domain.OAuth2TempAttributes;
import kr.co.pawpaw.domainredis.auth.repository.OAuth2TempAttributesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OAuth2TempAttributesCommand {
    private final OAuth2TempAttributesRepository oAuth2TempAttributesRepository;

    public OAuth2TempAttributes save(final OAuth2TempAttributes entity) {
        return oAuth2TempAttributesRepository.save(entity);
    }

    public void deleteById(final String id) {
        oAuth2TempAttributesRepository.deleteById(id);
    }
}
