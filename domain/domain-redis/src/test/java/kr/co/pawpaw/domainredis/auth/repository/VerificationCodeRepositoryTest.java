package kr.co.pawpaw.domainredis.auth.repository;

import kr.co.pawpaw.domainredis.auth.domain.VerificationCode;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import redis.embedded.RedisServer;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataRedisTest
class VerificationCodeRepositoryTest {
    @Autowired
    private VerificationCodeRepository verificationCodeRepository;
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
        verificationCodeRepository.deleteAll();
    }

    @Test
    @DisplayName("저장 및 불러오기 테스트")
    void saveAndFindTest() {
        //given
        VerificationCode input1 = VerificationCode.builder()
            .phoneNumber("12344321")
            .usagePurpose("SIGN_UP")
            .code("123456")
            .build();
        VerificationCode input2 = VerificationCode.builder()
            .phoneNumber("432111234")
            .usagePurpose("SIGN_UP")
            .code("654321")
            .build();

        String notSavedKey = VerificationCode.getCompositeKey("43214321", "SIGN_UP");

        verificationCodeRepository.saveAll(List.of(input1, input2));
        //when
        Optional<VerificationCode> result1 = verificationCodeRepository.findById(notSavedKey);
        Optional<VerificationCode> result2 = verificationCodeRepository.findById(input2.getId());
        Optional<VerificationCode> result3 = verificationCodeRepository.findById(input1.getId());

        //then
        assertThat(result1.isPresent()).isFalse();
        assertThat(result2.isPresent()).isTrue();
        assertThat(result3.isPresent()).isTrue();
        assertThat(result2.get().getPhoneNumber()).isEqualTo(input2.getPhoneNumber());
        assertThat(result2.get().getUsagePurpose()).isEqualTo(input2.getUsagePurpose());
        assertThat(result2.get().getCode()).isEqualTo(input2.getCode());
        assertThat(result3.get().getCode()).isEqualTo(input1.getCode());
        assertThat(result3.get().getPhoneNumber()).isEqualTo(input1.getPhoneNumber());
        assertThat(result3.get().getUsagePurpose()).isEqualTo(input1.getUsagePurpose());
    }

    @Test
    @DisplayName("existsByIdAndCode 메소드 테스트")
    void existsByIdAndCode() {
        //given
        VerificationCode input1 = VerificationCode.builder()
            .phoneNumber("12344321")
            .usagePurpose("SIGN_UP")
            .code("123456")
            .build();
        VerificationCode input2 = VerificationCode.builder()
            .phoneNumber("432111234")
            .usagePurpose("SIGN_UP")
            .code("654321")
            .build();


        verificationCodeRepository.saveAll(List.of(input1, input2));
        //when
        boolean result1 = verificationCodeRepository.existsByIdAndCode(input2.getId(), input2.getCode());
        boolean result2 = verificationCodeRepository.existsByIdAndCode(input1.getId(), input1.getCode());
        boolean result3 = verificationCodeRepository.existsByIdAndCode(input1.getId(), input2.getCode());
        boolean result4 = verificationCodeRepository.existsByIdAndCode(input2.getId(), input1.getCode());

        //then
        assertThat(result1).isTrue();
        assertThat(result2).isTrue();
        assertThat(result3).isFalse();
        assertThat(result4).isFalse();
    }
}