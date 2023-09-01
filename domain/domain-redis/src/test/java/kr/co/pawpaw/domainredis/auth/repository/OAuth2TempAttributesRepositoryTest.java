package kr.co.pawpaw.domainredis.auth.repository;

import kr.co.pawpaw.domainredis.auth.domain.OAuth2TempAttributes;
import kr.co.pawpaw.domainredis.auth.repository.OAuth2TempAttributesRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import redis.embedded.RedisServer;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@DataRedisTest
class OAuth2TempAttributesRepositoryTest {
    @Autowired
    private OAuth2TempAttributesRepository oAuth2TempAttributesRepository;
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
        oAuth2TempAttributesRepository.deleteAll();
    }

    @Test
    @DisplayName("저장 및 불러오기 테스트")
    void saveAndFindByIdTest() {
        //given
        OAuth2TempAttributes oAuth2TempAttributes = OAuth2TempAttributes.builder()
            .email("email")
            .name("name")
            .profileImageUrl("profileImageUrl")
            .provider("GOOGLE")
            .build();

        OAuth2TempAttributes oAuth2TempAttributes2 = OAuth2TempAttributes.builder()
            .email("email2")
            .name("name2")
            .profileImageUrl("profileImageUrl2")
            .provider("NAVER")
            .build();

        //when
        oAuth2TempAttributesRepository.save(oAuth2TempAttributes);
        Optional<OAuth2TempAttributes> newOAuth2TempAttributes = oAuth2TempAttributesRepository.findById(oAuth2TempAttributes.getKey());

        //then
        assertThat(newOAuth2TempAttributes.isPresent()).isTrue();
        assertThat(newOAuth2TempAttributes.get().getKey()).isEqualTo(oAuth2TempAttributes.getKey());
        assertThat(newOAuth2TempAttributes.get().getEmail()).isEqualTo(oAuth2TempAttributes.getEmail());
        assertThat(newOAuth2TempAttributes.get().getName()).isEqualTo(oAuth2TempAttributes.getName());
        assertThat(newOAuth2TempAttributes.get().getProfileImageUrl()).isEqualTo(oAuth2TempAttributes.getProfileImageUrl());
        assertThat(newOAuth2TempAttributes.get().getProvider()).isEqualTo(oAuth2TempAttributes.getProvider());
        assertThat(newOAuth2TempAttributes.get().getKey()).isNotEqualTo(oAuth2TempAttributes2.getKey());
        assertThat(newOAuth2TempAttributes.get().getEmail()).isNotEqualTo(oAuth2TempAttributes2.getEmail());
        assertThat(newOAuth2TempAttributes.get().getName()).isNotEqualTo(oAuth2TempAttributes2.getName());
        assertThat(newOAuth2TempAttributes.get().getProfileImageUrl()).isNotEqualTo(oAuth2TempAttributes2.getProfileImageUrl());
        assertThat(newOAuth2TempAttributes.get().getProvider()).isNotEqualTo(oAuth2TempAttributes2.getProvider());
    }

    @Test
    @DisplayName("deleteById 메서드 테스트")
    void deleteById() {
        //given
        OAuth2TempAttributes oAuth2TempAttributes = OAuth2TempAttributes.builder()
            .email("email")
            .name("name")
            .profileImageUrl("profileImageUrl")
            .provider("GOOGLE")
            .build();

        oAuth2TempAttributesRepository.save(oAuth2TempAttributes);

        //when
        oAuth2TempAttributesRepository.deleteById(oAuth2TempAttributes.getKey());
        boolean result = oAuth2TempAttributesRepository.existsById(oAuth2TempAttributes.getKey());

        //then
        assertThat(result).isFalse();
    }
}