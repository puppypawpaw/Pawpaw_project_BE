package kr.co.pawpaw.domainredis.auth.service.command;

import kr.co.pawpaw.domainredis.auth.domain.VerificationCode;
import kr.co.pawpaw.domainredis.auth.repository.VerificationCodeRepository;
import kr.co.pawpaw.domainredis.config.properties.VerificationLifeTimeProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VerificationCodeCommand {
    private final VerificationCodeRepository verificationCodeRepository;
    private final VerificationLifeTimeProperties verificationLifeTimeProperties;

    public VerificationCode save(final VerificationCode verificationCode) {
        verificationCode.updateTtl(verificationLifeTimeProperties.getCode());
        return verificationCodeRepository.save(verificationCode);
    }

    public void deleteByPhoneNumberAndUsagePurpose(final String phoneNumber, final String usagePurpose) {
        verificationCodeRepository.deleteById(VerificationCode.getCompositeKey(phoneNumber, usagePurpose));
    }

    public void deleteById(final String compositeKey) {
        verificationCodeRepository.deleteById(compositeKey);
    }
}
