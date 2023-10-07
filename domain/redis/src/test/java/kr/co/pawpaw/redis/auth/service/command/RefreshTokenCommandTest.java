package kr.co.pawpaw.redis.auth.service.command;

import kr.co.pawpaw.redis.auth.domain.RefreshToken;
import kr.co.pawpaw.redis.auth.repository.RefreshTokenRepository;
import kr.co.pawpaw.redis.config.property.TtlProperties;
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
    @Mock
    private TtlProperties ttlProperties;
    @InjectMocks
    private RefreshTokenCommand refreshTokenCommand;

    @Test
    @DisplayName("save 메서드 테스트")
    void save() {
        //given
        RefreshToken input = RefreshToken.builder()
            .userId(UUID.randomUUID().toString())
            .value("refreshTokenValue")
            .build();

        RefreshToken notInput = RefreshToken.builder()
            .userId(UUID.randomUUID().toString())
            .value("refreshTokenValue2")
            .build();

        Long ttl = 360L;

        when(ttlProperties.getRefreshToken()).thenReturn(ttl);
        when(refreshTokenRepository.save(any(RefreshToken.class))).thenReturn(input);

        //when
        RefreshToken result = refreshTokenCommand.save(input);

        //then
        verify(refreshTokenRepository).save(input);
        verify(ttlProperties).getRefreshToken();
        assertThat(result).isEqualTo(input);
        assertThat(result).isNotEqualTo(notInput);
        assertThat(result.getTtl()).isEqualTo(ttl);
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