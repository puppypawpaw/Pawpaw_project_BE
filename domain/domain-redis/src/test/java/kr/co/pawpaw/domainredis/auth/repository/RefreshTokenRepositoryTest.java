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
    @DisplayName("findByValue 메소드 테스트")
    void findByValue() {
        //given
        RefreshToken refreshToken = RefreshToken.builder()
            .userId(UUID.randomUUID().toString())
            .value("refreshTokenValue")
            .timeout(3600L)
            .build();

        RefreshToken refreshToken2 = RefreshToken.builder()
            .userId(UUID.randomUUID().toString())
            .value("refreshTokenValue2")
            .timeout(3601L)
            .build();

        refreshTokenRepository.saveAll(List.of(refreshToken, refreshToken2));

        //when
        Optional<RefreshToken> newRefreshToken = refreshTokenRepository.findByValue(refreshToken.getValue());

        //then
        assertThat(newRefreshToken.isPresent()).isTrue();
        assertThat(newRefreshToken.get().getValue()).isEqualTo(refreshToken.getValue());
        assertThat(newRefreshToken.get().getUserId()).isEqualTo(refreshToken.getUserId());
        assertThat(newRefreshToken.get().getTimeout()).isEqualTo(refreshToken.getTimeout());
        assertThat(newRefreshToken.get().getValue()).isNotEqualTo(refreshToken2.getValue());
        assertThat(newRefreshToken.get().getUserId()).isNotEqualTo(refreshToken2.getUserId());
        assertThat(newRefreshToken.get().getTimeout()).isNotEqualTo(refreshToken2.getTimeout());
    }

    @Test
    @DisplayName("existsByValue 메소드 테스트")
    void existsByValue() {
        //given
        RefreshToken refreshToken = RefreshToken.builder()
            .userId(UUID.randomUUID().toString())
            .value("refreshTokenValue")
            .timeout(3600L)
            .build();

        RefreshToken refreshToken2 = RefreshToken.builder()
            .userId(UUID.randomUUID().toString())
            .value("refreshTokenValue2")
            .timeout(3601L)
            .build();

        refreshTokenRepository.saveAll(List.of(refreshToken));

        //when
        boolean result1 = refreshTokenRepository.existsByValue(refreshToken.getValue());
        boolean result2 = refreshTokenRepository.existsByValue(refreshToken2.getValue());

        //then
        assertThat(result1).isTrue();
        assertThat(result2).isFalse();
    }

    @Test
    @DisplayName("timeout 테스트")
    void timeoutTest() throws InterruptedException {
        //given
        RefreshToken refreshToken1 = RefreshToken.builder()
            .userId(UUID.randomUUID().toString())
            .value("refreshTokenValue")
            .timeout(3L)
            .build();

        RefreshToken refreshToken2 = RefreshToken.builder()
            .userId(UUID.randomUUID().toString())
            .value("refreshTokenValue2")
            .timeout(1L)
            .build();

        refreshTokenRepository.saveAll(List.of(refreshToken1, refreshToken2));

        //when
        Thread.sleep(2000L);

        Optional<RefreshToken> findRefreshToken1 = refreshTokenRepository.findByValue(refreshToken1.getValue());
        Optional<RefreshToken> findRefreshToken2 = refreshTokenRepository.findByValue(refreshToken2.getValue());

        //then
        assertThat(findRefreshToken1.isPresent()).isTrue();
        assertThat(findRefreshToken2.isPresent()).isFalse();
    }
}