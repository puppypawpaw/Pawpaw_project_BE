package kr.co.pawpaw.mysql.storage.service.command;

import kr.co.pawpaw.mysql.storage.domain.File;
import kr.co.pawpaw.mysql.storage.repository.FileRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@Nested
@ExtendWith(MockitoExtension.class)
@DisplayName("FileCommand의")
class FileCommandTest {
    @Mock
    FileRepository fileRepository;
    @InjectMocks
    FileCommand fileCommand;

    @Test
    @DisplayName("save 메서드 테스트")
    void saveTest() {
        //given
        File file = File.builder()
            .fileName(UUID.randomUUID().toString())
            .build();
        when(fileRepository.save(eq(file))).thenReturn(file);

        //when
        File returnFile = fileCommand.save(file);

        //then
        verify(fileRepository).save(file);
        assertThat(returnFile).isEqualTo(file);
    }

    @Nested
    @DisplayName("deleteById 메서드는")
    class DeleteById {
        @Nested
        @DisplayName("FileRepository의")
        class FileRepository {
            @Test
            @DisplayName("deleteById 메서드를 호출한다.")
            void deleteById() {
                //given
                String fileName = UUID.randomUUID().toString();

                //when
                fileCommand.deleteById(fileName);

                //then
                verify(fileRepository).deleteById(fileName);
            }
        }
    }

    @Nested
    @DisplayName("saveAll 메서드는")
    class SaveAll {
        @Nested
        @DisplayName("FileRepository의")
        class FileRepository {
            @Test
            @DisplayName("saveAll 메서드를 호출한다.")
            void saveAll() {
                //given
                List<File> files = List.of(File.builder()
                    .fileName(UUID.randomUUID().toString())
                    .build());

                when(fileRepository.saveAll(files)).thenReturn(files);
                //when
                List<File> result = fileCommand.saveAll(files);

                //then
                verify(fileRepository).saveAll(files);
                assertThat(result).usingRecursiveComparison().isEqualTo(files);
            }
        }
    }

}