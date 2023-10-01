package kr.co.pawpaw.redis.auth.service.query;

import kr.co.pawpaw.redis.auth.domain.ChangePasswordTempKey;
import kr.co.pawpaw.redis.auth.repository.ChangePasswordTempKeyRepository;
import kr.co.pawpaw.redis.auth.service.query.ChangePasswordTempKeyQuery;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChangePasswordTempKeyQueryTest {
    @Mock
    private ChangePasswordTempKeyRepository changePasswordTempKeyRepository;
    @InjectMocks
    private ChangePasswordTempKeyQuery changePasswordTempKeyQuery;

    @Test
    @DisplayName("findChangePasswordTempKeyByKey 메서드는 changePasswordTempKeyRepository의 findById메서드를 호출한다.")
    void findChangePasswordTempKeyByKey() {
        //given
        ChangePasswordTempKey changePasswordTempKey = ChangePasswordTempKey.builder()
            .userId("userId")
            .build();
        when(changePasswordTempKeyRepository.findById(changePasswordTempKey.getKey())).thenReturn(Optional.of(changePasswordTempKey));

        //when
        Optional<ChangePasswordTempKey> result = changePasswordTempKeyQuery.findChangePasswordTempKeyByKey(changePasswordTempKey.getKey());

        //then
        assertThat(result.isPresent()).isTrue();
        assertThat(result.get()).usingRecursiveComparison().isEqualTo(changePasswordTempKey);
    }
}