package kr.co.pawpaw.redis.auth.service.command;

import kr.co.pawpaw.redis.auth.domain.VerificationCode;
import kr.co.pawpaw.redis.auth.repository.VerificationCodeRepository;
import kr.co.pawpaw.redis.config.property.TtlProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VerificationCodeCommand {
    private final VerificationCodeRepository verificationCodeRepository;
    private final TtlProperties ttlProperties;

    public VerificationCode save(final VerificationCode verificationCode) {
        verificationCode.updateTtl(ttlProperties.getVerificationLifeTimeCode());
        return verificationCodeRepository.save(verificationCode);
    }

    public void deleteByPhoneNumberAndUsagePurpose(final String phoneNumber, final String usagePurpose) {
        verificationCodeRepository.deleteById(VerificationCode.getCompositeKey(phoneNumber, usagePurpose));
    }

    public void deleteById(final String compositeKey) {
        verificationCodeRepository.deleteById(compositeKey);
    }
}
