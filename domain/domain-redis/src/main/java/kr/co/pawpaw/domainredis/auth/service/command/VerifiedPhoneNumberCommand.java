package kr.co.pawpaw.domainredis.auth.service.command;

import kr.co.pawpaw.domainredis.auth.domain.VerifiedPhoneNumber;
import kr.co.pawpaw.domainredis.auth.repository.VerifiedPhoneNumberRepository;
import kr.co.pawpaw.domainredis.config.properties.VerificationLifeTimeProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VerifiedPhoneNumberCommand {
    private final VerifiedPhoneNumberRepository verifiedPhoneNumberRepository;
    private final VerificationLifeTimeProperties verificationLifeTimeProperties;

    public VerifiedPhoneNumber save(final VerifiedPhoneNumber verifiedPhoneNumber) {
        updateTtl(verifiedPhoneNumber);
        return verifiedPhoneNumberRepository.save(verifiedPhoneNumber);
    }

    private void updateTtl(VerifiedPhoneNumber verifiedPhoneNumber) {
        switch (verifiedPhoneNumber.getUsagePurpose()) {
            case "SIGN_UP":
                verifiedPhoneNumber.updateTtl(verificationLifeTimeProperties.getSignup());
                break;
            default:
                verifiedPhoneNumber.updateTtl(verificationLifeTimeProperties.getDefaultTtl());
                break;
        }
    }
}
