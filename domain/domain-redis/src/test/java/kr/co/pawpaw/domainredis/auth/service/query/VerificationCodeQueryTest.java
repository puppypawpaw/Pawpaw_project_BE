package kr.co.pawpaw.domainredis.auth.service.query;

import kr.co.pawpaw.domainredis.auth.domain.VerificationCode;
import kr.co.pawpaw.domainredis.auth.repository.VerificationCodeRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VerificationCodeQueryTest {
    @Mock
    private VerificationCodeRepository verificationCodeRepository;
    @InjectMocks
    private VerificationCodeQuery verificationCodeQuery;

    @Test
    @DisplayName("existsByPhoneNumberAndUsagePurposeAndCode 메소드 테스트")
    void existsByPhoneNumberAndUsagePurposeAndCode() {
        //given
        String phoneNumber = "123321";
        String usagePurpose = "SIGN_UP";
        String code = "123321";
        VerificationCode vCode = VerificationCode.builder()
            .code(code)
            .phoneNumber(phoneNumber)
            .usagePurpose(usagePurpose)
            .build();
        when(verificationCodeRepository.existsByIdAndCode(eq(vCode.getId()), eq(vCode.getCode()))).thenReturn(true);
        //when
        boolean result = verificationCodeQuery.existsByPhoneNumberAndUsagePurposeAndCode(phoneNumber, usagePurpose, code);

        //then
        verify(verificationCodeRepository).existsByIdAndCode(vCode.getId(), code);
        assertThat(result).isTrue();
    }
}