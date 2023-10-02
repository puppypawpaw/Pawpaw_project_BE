package kr.co.pawpaw.redis.auth.service.command;

import kr.co.pawpaw.redis.auth.domain.OAuth2TempAttributes;
import kr.co.pawpaw.redis.auth.repository.OAuth2TempAttributesRepository;
import kr.co.pawpaw.redis.auth.service.command.OAuth2TempAttributesCommand;
import kr.co.pawpaw.redis.config.property.TtlProperties;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OAuth2TempAttributesCommandTest {
    @Mock
    private OAuth2TempAttributesRepository oAuth2TempAttributesRepository;
    @Mock
    private TtlProperties ttlProperties;
    @InjectMocks
    private OAuth2TempAttributesCommand oAuth2TempAttributesCommand;

    @Test
    @DisplayName("save 메서드 테스트")
    void save() {
        //given
        OAuth2TempAttributes input = OAuth2TempAttributes.builder()
            .email("email")
            .name("name")
            .profileImageUrl("profileImageUrl")
            .provider("GOOGLE")
            .build();

        Long ttl = 360L;

        when(ttlProperties.getOauth2TempAttributes()).thenReturn(ttl);
        when(oAuth2TempAttributesRepository.save(any(OAuth2TempAttributes.class))).thenReturn(input);

        //when
        OAuth2TempAttributes result = oAuth2TempAttributesCommand.save(input);

        //then
        verify(oAuth2TempAttributesRepository, times(1)).save(input);
        verify(ttlProperties).getOauth2TempAttributes();
        assertThat(input.getTtl()).isEqualTo(ttl);
        assertThat(result).isEqualTo(input);
    }

    @Test
    @DisplayName("deleteById 메서드 테스트")
    void deleteById() {
        //given
        OAuth2TempAttributes input = OAuth2TempAttributes.builder()
            .email("email")
            .name("name")
            .profileImageUrl("profileImageUrl")
            .provider("GOOGLE")
            .build();

        //when
        oAuth2TempAttributesCommand.deleteById(input.getKey());

        //then
        verify(oAuth2TempAttributesRepository, times(1)).deleteById(input.getKey());
    }
}