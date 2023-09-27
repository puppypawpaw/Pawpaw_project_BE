package kr.co.pawpaw.domainredis.auth.repository;

import kr.co.pawpaw.domainredis.auth.domain.RefreshToken;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import redis.embedded.RedisServer;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataRedisTest
class RefreshTokenRepositoryTest {
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    private static RedisServer redisServer;

    @BeforeAll
    static void startRedisServer() {
        redisServer = new RedisServer();
        redisServer.start();
    }

    @AfterAll
    static void endRedisServer() {
        redisServer.stop();
    }

    @BeforeEach
    void beforeEach() {
        refreshTokenRepository.deleteAll();
    }

    @Test
    @DisplayName("findByValue 메서드 테스트")
    void findByValue() {
        //given
        RefreshToken refreshToken = RefreshToken.builder()
            .userId(UUID.randomUUID().toString())
            .value("refreshTokenValue")
            .build();

        Long ttl1 = 1L;
        Long ttl2 = 2L;

        refreshToken.updateTtl(ttl1);

        RefreshToken refreshToken2 = RefreshToken.builder()
            .userId(UUID.randomUUID().toString())
            .value("refreshTokenValue2")
            .build();

        refreshToken2.updateTtl(ttl2);

        refreshTokenRepository.saveAll(List.of(refreshToken, refreshToken2));

        //when
        Optional<RefreshToken> newRefreshToken = refreshTokenRepository.findByValue(refreshToken.getValue());

        //then
        assertThat(newRefreshToken.isPresent()).isTrue();
        assertThat(newRefreshToken.get().getValue()).isEqualTo(refreshToken.getValue());
        assertThat(newRefreshToken.get().getUserId()).isEqualTo(refreshToken.getUserId());
        assertThat(newRefreshToken.get().getTtl()).isEqualTo(refreshToken.getTtl());
        assertThat(newRefreshToken.get().getValue()).isNotEqualTo(refreshToken2.getValue());
        assertThat(newRefreshToken.get().getUserId()).isNotEqualTo(refreshToken2.getUserId());
        assertThat(newRefreshToken.get().getTtl()).isNotEqualTo(refreshToken2.getTtl());
    }

    @Test
    @DisplayName("existsByValue 메서드 테스트")
    void existsByValue() {
        //given
        RefreshToken refreshToken = RefreshToken.builder()
            .userId(UUID.randomUUID().toString())
            .value("refreshTokenValue")
            .build();

        RefreshToken refreshToken2 = RefreshToken.builder()
            .userId(UUID.randomUUID().toString())
            .value("refreshTokenValue2")
            .build();

        refreshTokenRepository.saveAll(List.of(refreshToken));

        //when
        boolean result1 = refreshTokenRepository.existsByValue(refreshToken.getValue());
        boolean result2 = refreshTokenRepository.existsByValue(refreshToken2.getValue());

        //then
        assertThat(result1).isTrue();
        assertThat(result2).isFalse();
    }
}