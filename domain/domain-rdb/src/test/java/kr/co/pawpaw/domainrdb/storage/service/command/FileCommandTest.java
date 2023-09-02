package kr.co.pawpaw.domainrdb.storage.service.command;

import kr.co.pawpaw.domainrdb.storage.domain.File;
import kr.co.pawpaw.domainrdb.storage.repository.FileRepository;
import kr.co.pawpaw.domainrdb.user.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FileCommandTest {
    @Mock
    FileRepository fileRepository;
    @InjectMocks
    FileCommand fileCommand;

    @Test
    @DisplayName("save 메소드 테스트")
    void saveTest() {
        //given
        File file = File.builder()
            .build();
        when(fileRepository.save(eq(file))).thenReturn(file);

        //when
        File returnFile = fileCommand.save(file);

        //then
        verify(fileRepository).save(file);
        assertThat(returnFile).isEqualTo(file);
    }
}