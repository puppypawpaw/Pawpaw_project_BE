package kr.co.pawpaw.redis.auth.service.query;

import kr.co.pawpaw.redis.auth.domain.RefreshToken;
import kr.co.pawpaw.redis.auth.repository.RefreshTokenRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RefreshTokenQueryTest {
    @Mock
    private RefreshTokenRepository refreshTokenRepository;
    @InjectMocks
    private RefreshTokenQuery refreshTokenQuery;

    @Test
    void findByValue() {
        //given
        RefreshToken input1 = RefreshToken.builder()
            .userId(UUID.randomUUID().toString())
            .value("refreshTokenValue")
            .build();

        RefreshToken input2 = RefreshToken.builder()
            .userId(UUID.randomUUID().toString())
            .value("refreshTokenValue2")
            .build();

        when(refreshTokenRepository.findByValue(eq(input1.getValue()))).thenReturn(Optional.of(input1));
        when(refreshTokenRepository.findByValue(eq(input2.getValue()))).thenReturn(Optional.of(input2));

        //when
        Optional<RefreshToken> result1 = refreshTokenQuery.findByValue(input1.getValue());
        Optional<RefreshToken> result2 = refreshTokenQuery.findByValue(input2.getValue());

        //then
        verify(refreshTokenRepository).findByValue(input1.getValue());
        verify(refreshTokenRepository).findByValue(input2.getValue());

        assertThat(result1.isPresent()).isTrue();
        assertThat(result2.isPresent()).isTrue();
        assertThat(result1.get()).isEqualTo(input1);
        assertThat(result2.get()).isEqualTo(input2);
    }

    @Test
    void existsByValue() {
        //given
        RefreshToken input1 = RefreshToken.builder()
            .userId(UUID.randomUUID().toString())
            .value("refreshTokenValue")
            .build();

        RefreshToken input2 = RefreshToken.builder()
            .userId(UUID.randomUUID().toString())
            .value("refreshTokenValue2")
            .build();

        when(refreshTokenRepository.existsByValue(eq(input1.getValue()))).thenReturn(true);
        when(refreshTokenRepository.existsByValue(eq(input2.getValue()))).thenReturn(true);

        //when
        boolean result1 = refreshTokenQuery.existsByValue(input1.getValue());
        boolean result2 = refreshTokenQuery.existsByValue(input2.getValue());

        //then
        verify(refreshTokenRepository).existsByValue(input1.getValue());
        verify(refreshTokenRepository).existsByValue(input2.getValue());

        assertThat(result1).isTrue();
        assertThat(result2).isTrue();
    }
}