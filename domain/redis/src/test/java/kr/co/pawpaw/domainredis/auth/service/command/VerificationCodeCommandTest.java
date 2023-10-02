package kr.co.pawpaw.redis.auth.service.command;

import kr.co.pawpaw.redis.auth.domain.VerificationCode;
import kr.co.pawpaw.redis.auth.repository.VerificationCodeRepository;
import kr.co.pawpaw.redis.auth.service.command.VerificationCodeCommand;
import kr.co.pawpaw.redis.config.property.TtlProperties;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VerificationCodeCommandTest {
    @Mock
    private VerificationCodeRepository verificationCodeRepository;
    @Mock
    private TtlProperties ttlProperties;
    @InjectMocks
    private VerificationCodeCommand verificationCodeCommand;

    @Test
    @DisplayName("save 메서드 테스트")
    void save() {
        //given
        VerificationCode input = VerificationCode.builder()
            .code("123456")
            .phoneNumber("123321")
            .usagePurpose("SIGN_UP")
            .build();
        Long ttl = 360L;

        when(ttlProperties.getVerificationLifeTimeCode()).thenReturn(ttl);
        when(verificationCodeRepository.save(eq(input))).thenReturn(input);
        //when
        VerificationCode result = verificationCodeCommand.save(input);

        //then
        verify(verificationCodeRepository).save(input);
        assertThat(result).isEqualTo(input);
        assertThat(result.getTtl()).isEqualTo(ttl);
    }

    @Test
    @DisplayName("deleteByPhoneNumberAndUsagePurpose 메서드 테스트")
    void deleteByPhoneNumberAndUsagePurpose() {
        //given
        VerificationCode input = VerificationCode.builder()
            .code("123456")
            .phoneNumber("123321")
            .usagePurpose("usage")
            .build();

        //when
        verificationCodeCommand.deleteByPhoneNumberAndUsagePurpose(input.getPhoneNumber(), input.getUsagePurpose());

        //then
        verify(verificationCodeRepository).deleteById(input.getId());
    }

    @Test
    @DisplayName("deleteById 메서드 verificationCodeRepository deleteById 메서드 호출 테스트")
    void deleteById() {
        //given
        String id = "id";

        //when
        verificationCodeCommand.deleteById(id);

        //then
        verify(verificationCodeRepository).deleteById(id);
    }
}