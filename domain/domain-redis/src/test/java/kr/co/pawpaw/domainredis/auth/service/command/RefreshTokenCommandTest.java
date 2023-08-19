package kr.co.pawpaw.domainredis.auth.service.command;

import kr.co.pawpaw.domainredis.auth.domain.RefreshToken;
import kr.co.pawpaw.domainredis.auth.repository.RefreshTokenRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RefreshTokenCommandTest {
    @Mock
    private RefreshTokenRepository refreshTokenRepository;
    @InjectMocks
    private RefreshTokenCommand refreshTokenCommand;

    @Test
    @DisplayName("save 메소드 테스트")
    void save() {
        //given
        RefreshToken input = RefreshToken.builder()
            .userId(UUID.randomUUID().toString())
            .value("refreshTokenValue")
            .timeout(3600L)
            .build();

        RefreshToken notInput = RefreshToken.builder()
            .userId(UUID.randomUUID().toString())
            .value("refreshTokenValue2")
            .timeout(3601L)
            .build();

        when(refreshTokenRepository.save(any(RefreshToken.class))).thenReturn(input);

        //when
        RefreshToken result = refreshTokenCommand.save(input);

        //then
        verify(refreshTokenRepository).save(input);
        assertThat(result).isEqualTo(input);
        assertThat(result).isNotEqualTo(notInput);
    }

    @Test
    void deleteById() {
        //given
        String userId = UUID.randomUUID().toString();

        //when
        refreshTokenCommand.deleteById(userId);

        //then
        verify(refreshTokenRepository).deleteById(userId);
    }
}