package kr.co.pawpaw.domainredis.auth.service.command;

import kr.co.pawpaw.domainredis.auth.domain.ChangePasswordTempKey;
import kr.co.pawpaw.domainredis.auth.repository.ChangePasswordTempKeyRepository;
import kr.co.pawpaw.domainredis.config.property.TtlProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChangePasswordTempKeyCommand {
    private final ChangePasswordTempKeyRepository changePasswordTempKeyRepository;
    private final TtlProperties ttlProperties;

    public ChangePasswordTempKey save(final ChangePasswordTempKey changePasswordTempKey) {
        changePasswordTempKey.updateTtl(ttlProperties.getChangePasswordTempKey());
        return changePasswordTempKeyRepository.save(changePasswordTempKey);
    }

    public void delete(final ChangePasswordTempKey changePasswordTempKey) {
        changePasswordTempKeyRepository.delete(changePasswordTempKey);
    }
}
