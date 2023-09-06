package kr.co.pawpaw.domainredis.auth.service.query;

import kr.co.pawpaw.domainredis.auth.domain.VerificationCode;
import kr.co.pawpaw.domainredis.auth.repository.VerificationCodeRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VerificationCodeQueryTest {
    @Mock
    private VerificationCodeRepository verificationCodeRepository;
    @InjectMocks
    private VerificationCodeQuery verificationCodeQuery;

    @Test
    @DisplayName("findByIdAndCode 메소드 테스트")
    void findByIdAndCode() {
        //given
        String phoneNumber = "123321";
        String usagePurpose = "SIGN_UP";
        String code = "123321";
        VerificationCode vCode = VerificationCode.builder()
            .code(code)
            .phoneNumber(phoneNumber)
            .usagePurpose(usagePurpose)
            .build();
        when(verificationCodeRepository.findByIdAndCode(eq(vCode.getId()), eq(vCode.getCode()))).thenReturn(Optional.of(vCode));
        //when
        Optional<VerificationCode> result = verificationCodeQuery.findByPhoneNumberAndUsagePurposeAndCode(phoneNumber, usagePurpose, code);

        //then
        verify(verificationCodeRepository).findByIdAndCode(vCode.getId(), code);
        assertThat(result.isPresent()).isTrue();
        assertThat(result.get()).usingRecursiveComparison().isEqualTo(vCode);
    }
}