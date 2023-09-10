package kr.co.pawpaw.domainredis.auth.service.query;

import kr.co.pawpaw.domainredis.auth.domain.VerificationCode;
import kr.co.pawpaw.domainredis.auth.repository.VerificationCodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VerificationCodeQuery {
    private final VerificationCodeRepository verificationCodeRepository;

    public Optional<VerificationCode> findByPhoneNumberAndUsagePurposeAndCode(
        final String phoneNumber,
        final String usagePurpose,
        final String code
    ) {
        return verificationCodeRepository.findByIdAndCode(VerificationCode.getCompositeKey(phoneNumber, usagePurpose), code);
    }
}
