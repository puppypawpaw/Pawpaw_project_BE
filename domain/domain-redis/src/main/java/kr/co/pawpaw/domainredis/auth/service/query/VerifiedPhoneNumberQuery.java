package kr.co.pawpaw.domainredis.auth.service.query;

import kr.co.pawpaw.domainredis.auth.domain.VerifiedPhoneNumber;
import kr.co.pawpaw.domainredis.auth.repository.VerifiedPhoneNumberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VerifiedPhoneNumberQuery {
    private final VerifiedPhoneNumberRepository verifiedPhoneNumberRepository;

    public boolean existsByPhoneNumberAndUsagePurpose(final String phoneNumber, final String usagePurpose) {
        return verifiedPhoneNumberRepository.existsById(VerifiedPhoneNumber.getCompositeKey(phoneNumber, usagePurpose));
    }
}
