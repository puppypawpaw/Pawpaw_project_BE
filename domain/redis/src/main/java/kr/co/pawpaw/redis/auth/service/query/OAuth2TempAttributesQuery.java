package kr.co.pawpaw.redis.auth.service.query;

import kr.co.pawpaw.redis.auth.domain.OAuth2TempAttributes;
import kr.co.pawpaw.redis.auth.repository.OAuth2TempAttributesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OAuth2TempAttributesQuery {
    private final OAuth2TempAttributesRepository oAuth2TempAttributesRepository;

    public Optional<OAuth2TempAttributes> findById(final String key) {
        return oAuth2TempAttributesRepository.findById(key);
    }
}
