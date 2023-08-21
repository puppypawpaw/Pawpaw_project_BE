package kr.co.pawpaw.domainredis.auth.repository;

import kr.co.pawpaw.domainredis.auth.domain.VerifiedPhoneNumber;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import redis.embedded.RedisServer;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DataRedisTest
class VerifiedPhoneNumberRepositoryTest {
    @Autowired
    private VerifiedPhoneNumberRepository verifiedPhoneNumberRepository;
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
        verifiedPhoneNumberRepository.deleteAll();
    }

    @Test
    @DisplayName("저장 및 불러오기 테스트")
    void saveAndFindTest() {
        //given
        VerifiedPhoneNumber input1 = VerifiedPhoneNumber.builder()
            .phoneNumber("123321123321")
            .usagePurpose("SIGN_UP")
            .build();

        VerifiedPhoneNumber input2 = VerifiedPhoneNumber.builder()
            .phoneNumber("789987789987")
            .usagePurpose("SIGN_UP")
            .build();

        verifiedPhoneNumberRepository.saveAll(List.of(input1, input2));
        //when
        Optional<VerifiedPhoneNumber> result1 = verifiedPhoneNumberRepository.findById(input2.getId());
        Optional<VerifiedPhoneNumber> result2 = verifiedPhoneNumberRepository.findById(input1.getId());

        //then
        assertThat(result1.isPresent()).isTrue();
        assertThat(result2.isPresent()).isTrue();
        assertThat(result1.get().getPhoneNumber()).isEqualTo(input2.getPhoneNumber());
        assertThat(result1.get().getUsagePurpose()).isEqualTo(input2.getUsagePurpose());
        assertThat(result2.get().getPhoneNumber()).isEqualTo(input1.getPhoneNumber());
        assertThat(result2.get().getUsagePurpose()).isEqualTo(input1.getUsagePurpose());
    }
}