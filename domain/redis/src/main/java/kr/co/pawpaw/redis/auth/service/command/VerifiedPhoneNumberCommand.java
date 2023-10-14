package kr.co.pawpaw.redis.auth.service.command;

import kr.co.pawpaw.redis.auth.domain.VerifiedPhoneNumber;
import kr.co.pawpaw.redis.auth.repository.VerifiedPhoneNumberRepository;
import kr.co.pawpaw.redis.config.property.TtlProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VerifiedPhoneNumberCommand {
    private final VerifiedPhoneNumberRepository verifiedPhoneNumberRepository;
    private final TtlProperties ttlProperties;

    public VerifiedPhoneNumber save(final VerifiedPhoneNumber verifiedPhoneNumber) {
        updateTtl(verifiedPhoneNumber);
        return verifiedPhoneNumberRepository.save(verifiedPhoneNumber);
    }

    private void updateTtl(VerifiedPhoneNumber verifiedPhoneNumber) {
        switch (verifiedPhoneNumber.getUsagePurpose()) {
            case "SIGN_UP":
                verifiedPhoneNumber.updateTtl(ttlProperties.getVerificationLifeTimeSignUp());
                break;
            default:
                verifiedPhoneNumber.updateTtl(ttlProperties.getVerificationLifeTimeDefault());
                break;
        }
    }
}
