package kr.co.pawpaw.mysql.storage.repository;

import kr.co.pawpaw.mysql.common.MySQLTestContainer;
import kr.co.pawpaw.mysql.storage.domain.File;
import kr.co.pawpaw.mysql.user.domain.User;
import kr.co.pawpaw.mysql.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("FileRepository는")
class FileRepositoryTest extends MySQLTestContainer {
    @Autowired
    private FileRepository fileRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("파일 저장 및 불러오기 테스트")
    void saveAndLoadTest() {
        //given
        User user = User.builder()
            .build();

        user = userRepository.save(user);

        File file = File.builder()
            .fileName(UUID.randomUUID().toString())
            .fileUrl("fileUrl")
            .byteSize(123456L)
            .uploader(user)
            .contentType("image/png")
            .build();

        //when
        file = fileRepository.save(file);
        Optional<File> loadFile = fileRepository.findById(file.getFileName());

        //then
        assertThat(loadFile.isPresent()).isTrue();
        assertThat(loadFile.get()).usingRecursiveComparison().isEqualTo(file);
    }
}