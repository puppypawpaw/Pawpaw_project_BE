package kr.co.pawpaw.redis.auth.service.query;

import kr.co.pawpaw.redis.auth.domain.VerificationCode;
import kr.co.pawpaw.redis.auth.repository.VerificationCodeRepository;
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
