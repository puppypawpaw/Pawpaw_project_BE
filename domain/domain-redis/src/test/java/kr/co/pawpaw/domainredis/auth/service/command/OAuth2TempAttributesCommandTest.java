package kr.co.pawpaw.domainredis.auth.service.command;

import kr.co.pawpaw.domainredis.auth.domain.OAuth2TempAttributes;
import kr.co.pawpaw.domainredis.auth.repository.OAuth2TempAttributesRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OAuth2TempAttributesCommandTest {
    @Mock
    private OAuth2TempAttributesRepository oAuth2TempAttributesRepository;
    @InjectMocks
    private OAuth2TempAttributesCommand oAuth2TempAttributesCommand;

    @Test
    @DisplayName("save 메소드 테스트")
    void save() {
        //given
        OAuth2TempAttributes input = OAuth2TempAttributes.builder()
            .email("email")
            .name("name")
            .profileImageUrl("profileImageUrl")
            .provider("GOOGLE")
            .build();

        when(oAuth2TempAttributesRepository.save(any(OAuth2TempAttributes.class))).thenReturn(input);

        //when
        OAuth2TempAttributes result = oAuth2TempAttributesCommand.save(input);

        //then
        verify(oAuth2TempAttributesRepository, times(1)).save(input);
        assertThat(result).isEqualTo(input);
    }

    @Test
    @DisplayName("deleteById 메소드 테스트")
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