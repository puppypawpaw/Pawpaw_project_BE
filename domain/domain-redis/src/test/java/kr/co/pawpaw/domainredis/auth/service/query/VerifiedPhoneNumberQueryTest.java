package kr.co.pawpaw.domainredis.auth.service.query;

import kr.co.pawpaw.domainredis.auth.domain.VerifiedPhoneNumber;
import kr.co.pawpaw.domainredis.auth.repository.VerifiedPhoneNumberRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VerifiedPhoneNumberQueryTest {
    @Mock
    private VerifiedPhoneNumberRepository verifiedPhoneNumberRepository;
    @InjectMocks
    private VerifiedPhoneNumberQuery verifiedPhoneNumberQuery;

    @Test
    void existsByPhoneNumberAndUsagePurpose() {
        // given
        String phoneNumber = "123321";
        String usagePurpose = "SIGN_UP";
        VerifiedPhoneNumber verifiedPhoneNumber = VerifiedPhoneNumber.builder()
            .phoneNumber(phoneNumber)
            .usagePurpose(usagePurpose)
            .build();

        when(verifiedPhoneNumberRepository.existsById(verifiedPhoneNumber.getId())).thenReturn(true);
        // when
        boolean result = verifiedPhoneNumberQuery.existsByPhoneNumberAndUsagePurpose(phoneNumber, usagePurpose);

        // then
        verify(verifiedPhoneNumberRepository).existsById(verifiedPhoneNumber.getId());
        assertThat(result).isTrue();
    }
}