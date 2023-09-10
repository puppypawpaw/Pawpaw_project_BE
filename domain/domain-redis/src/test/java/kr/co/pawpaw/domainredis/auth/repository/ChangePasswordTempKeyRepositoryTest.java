package kr.co.pawpaw.domainredis.auth.repository;

import kr.co.pawpaw.domainredis.auth.domain.ChangePasswordTempKey;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import redis.embedded.RedisServer;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DataRedisTest
class ChangePasswordTempKeyRepositoryTest {
    @Autowired
    private ChangePasswordTempKeyRepository changePasswordTempKeyRepository;
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
        changePasswordTempKeyRepository.deleteAll();
    }

    @Test
    @DisplayName("저장 및 불러오기 테스트")
    void saveAndLoad() {
        //given
        ChangePasswordTempKey changePasswordTempKey = ChangePasswordTempKey.builder()
            .userId("userId")
            .build();

        Long ttl = 321L;

        changePasswordTempKey.updateTtl(ttl);

        //when
        changePasswordTempKeyRepository.save(changePasswordTempKey);
        Optional<ChangePasswordTempKey> result = changePasswordTempKeyRepository.findById(changePasswordTempKey.getKey());

        //then
        assertThat(result.isPresent()).isTrue();
        assertThat(result.get()).usingRecursiveComparison().isEqualTo(changePasswordTempKey);
    }
}