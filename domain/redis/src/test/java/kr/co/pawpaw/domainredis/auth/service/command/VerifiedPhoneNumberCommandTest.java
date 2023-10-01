package kr.co.pawpaw.redis.auth.service.command;

import kr.co.pawpaw.redis.auth.domain.VerifiedPhoneNumber;
import kr.co.pawpaw.redis.auth.repository.VerifiedPhoneNumberRepository;
import kr.co.pawpaw.redis.auth.service.command.VerifiedPhoneNumberCommand;
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
class VerifiedPhoneNumberCommandTest {
    @Mock
    private VerifiedPhoneNumberRepository verifiedPhoneNumberRepository;
    @Mock
    private TtlProperties ttlProperties;
    @InjectMocks
    private VerifiedPhoneNumberCommand verifiedPhoneNumberCommand;

    @Test
    @DisplayName("저장 및 불러오기 테스트")
    void saveAndFindTest() {
        //given
        VerifiedPhoneNumber input = VerifiedPhoneNumber.builder()
            .phoneNumber("123321")
            .usagePurpose("SIGN_UP")
            .build();
        Long ttl = 1234L;

        when(ttlProperties.getVerificationLifeTimeSignUp()).thenReturn(ttl);
        when(verifiedPhoneNumberRepository.save(eq(input))).thenReturn(input);
        //when
        VerifiedPhoneNumber result = verifiedPhoneNumberCommand.save(input);

        //then
        verify(verifiedPhoneNumberRepository).save(input);
        assertThat(result.getTtl()).isEqualTo(1234L);
        assertThat(result).isEqualTo(input);
    }
}