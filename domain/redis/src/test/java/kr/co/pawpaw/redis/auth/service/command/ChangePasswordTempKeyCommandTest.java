package kr.co.pawpaw.redis.auth.service.command;

import kr.co.pawpaw.redis.auth.domain.ChangePasswordTempKey;
import kr.co.pawpaw.redis.auth.repository.ChangePasswordTempKeyRepository;
import kr.co.pawpaw.redis.config.property.TtlProperties;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ChangePasswordTempKeyCommandTest {
    @Mock
    private ChangePasswordTempKeyRepository changePasswordTempKeyRepository;
    @Mock
    private TtlProperties ttlProperties;
    @InjectMocks
    private ChangePasswordTempKeyCommand changePasswordTempKeyCommand;

    @Test
    @DisplayName("save 메서드는 ttl을 업데이트 한다.")
    void saveUpdateTtl() {
        //given
        ChangePasswordTempKey changePasswordTempKey = ChangePasswordTempKey.builder()
            .userId("userId")
            .build();

        Long ttl = 1234L;

        when(ttlProperties.getChangePasswordTempKey()).thenReturn(ttl);
        when(changePasswordTempKeyRepository.save(changePasswordTempKey)).thenReturn(changePasswordTempKey);

        //when
        changePasswordTempKeyCommand.save(changePasswordTempKey);

        //then
        assertThat(changePasswordTempKey.getTtl()).isEqualTo(ttl);
    }

    @Test
    @DisplayName("save 메서드는 ChangePasswordTempKeyRepository의 save 메서드를 호출한다.")
    void saveRepositorySave() {
        //given
        ChangePasswordTempKey changePasswordTempKey = ChangePasswordTempKey.builder()
            .userId("userId")
            .build();

        Long ttl = 1234L;

        when(ttlProperties.getChangePasswordTempKey()).thenReturn(ttl);
        when(changePasswordTempKeyRepository.save(changePasswordTempKey)).thenReturn(changePasswordTempKey);

        //when
        changePasswordTempKeyCommand.save(changePasswordTempKey);

        //then
        verify(changePasswordTempKeyRepository).save(changePasswordTempKey);
    }

    @Test
    @DisplayName("delete 메서드는 changePasswordTempKeyRepository의 delete 메서드를 호출한다.")
    void delete() {
        //given
        ChangePasswordTempKey changePasswordTempKey = ChangePasswordTempKey.builder().build();

        //when
        changePasswordTempKeyCommand.delete(changePasswordTempKey);

        //then
        verify(changePasswordTempKeyRepository).delete(changePasswordTempKey);
    }
}