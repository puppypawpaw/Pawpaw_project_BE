package kr.co.pawpaw.domainredis.auth.service.query;

import kr.co.pawpaw.domainredis.auth.domain.VerificationCode;
import kr.co.pawpaw.domainredis.auth.repository.VerificationCodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VerificationCodeQuery {
    private final VerificationCodeRepository verificationCodeRepository;

    public boolean existsByPhoneNumberAndUsagePurposeAndCode(
        final String phoneNumber,
        final String usagePurpose,
        final String code
    ) {
        return verificationCodeRepository.existsByIdAndCode(VerificationCode.getCompositeKey(phoneNumber, usagePurpose), code);
    }
}
