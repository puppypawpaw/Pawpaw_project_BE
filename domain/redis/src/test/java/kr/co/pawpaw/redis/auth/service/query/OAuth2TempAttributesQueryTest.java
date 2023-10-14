package kr.co.pawpaw.redis.auth.service.query;

import kr.co.pawpaw.redis.auth.domain.OAuth2TempAttributes;
import kr.co.pawpaw.redis.auth.repository.OAuth2TempAttributesRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OAuth2TempAttributesQueryTest {
    @Mock
    private OAuth2TempAttributesRepository oAuth2TempAttributesRepository;
    @InjectMocks
    private OAuth2TempAttributesQuery oAuth2TempAttributesQuery;

    @Test
    @DisplayName("findById 메서드 테스트")
    void findById() {
        //given
        OAuth2TempAttributes input = OAuth2TempAttributes.builder()
            .email("email")
            .name("name")
            .profileImageUrl("profileImageUrl")
            .provider("GOOGLE")
            .build();

        when(oAuth2TempAttributesRepository.findById(any(String.class))).thenReturn(Optional.of(input));

        //when
        Optional<OAuth2TempAttributes> result = oAuth2TempAttributesQuery.findById(input.getKey());

        //then
        verify(oAuth2TempAttributesRepository).findById(input.getKey());
        assertThat(result.isPresent()).isTrue();
        assertThat(result.get()).isEqualTo(input);
    }
}