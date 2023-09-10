package kr.co.pawpaw.domainredis.auth.service.query;

import kr.co.pawpaw.domainredis.auth.domain.ChangePasswordTempKey;
import kr.co.pawpaw.domainredis.auth.repository.ChangePasswordTempKeyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChangePasswordTempKeyQuery {
    private final ChangePasswordTempKeyRepository changePasswordTempKeyRepository;

    public Optional<ChangePasswordTempKey> findChangePasswordTempKeyByKey(final String key) {
        return changePasswordTempKeyRepository.findById(key);
    }
}
