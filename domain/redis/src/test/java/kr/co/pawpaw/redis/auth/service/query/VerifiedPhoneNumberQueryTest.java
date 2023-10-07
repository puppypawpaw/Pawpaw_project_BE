package kr.co.pawpaw.redis.auth.service.query;

import kr.co.pawpaw.redis.auth.domain.VerifiedPhoneNumber;
import kr.co.pawpaw.redis.auth.repository.VerifiedPhoneNumberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

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
    @DisplayName("existsByPhoneNumberAndUsagePurpose 메서드 테스트")
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

    @Test
    @DisplayName("findByPhoneNumberAndUsagePurpose 메서드 verifiedPhoneNumberRepository findById 메서드 호출 테스트")
    void findByPhoneNumberAndUsagePurpose() {
        //given
        VerifiedPhoneNumber vPhoneNo = VerifiedPhoneNumber.builder()
            .userName("userName")
            .usagePurpose("usagePurpose")
            .phoneNumber("phoneNo")
            .build();
        when(verifiedPhoneNumberRepository.findById(vPhoneNo.getId())).thenReturn(Optional.of(vPhoneNo));

        //when
        Optional<VerifiedPhoneNumber> result = verifiedPhoneNumberQuery.findByPhoneNumberAndUsagePurpose(vPhoneNo.getPhoneNumber(), vPhoneNo.getUsagePurpose());

        //then
        assertThat(result.isPresent()).isTrue();
        assertThat(result.get()).usingRecursiveComparison().isEqualTo(vPhoneNo);
    }
}