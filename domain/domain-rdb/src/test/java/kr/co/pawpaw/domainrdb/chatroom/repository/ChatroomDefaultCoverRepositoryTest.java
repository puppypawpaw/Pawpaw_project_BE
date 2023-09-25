package kr.co.pawpaw.domainrdb.chatroom.repository;

import kr.co.pawpaw.domainrdb.chatroom.domain.ChatroomDefaultCover;
import kr.co.pawpaw.domainrdb.storage.domain.File;
import kr.co.pawpaw.domainrdb.storage.repository.FileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@Nested
@DisplayName("ChatroomDefaultCoverRepository의")
class ChatroomDefaultCoverRepositoryTest {
    @Autowired
    private ChatroomDefaultCoverRepository chatroomDefaultCoverRepository;
    @Autowired
    private FileRepository fileRepository;

    private File file = File.builder()
        .fileName("fileName")
        .fileUrl("fileUrl")
        .contentType("test")
        .byteSize(123L)
        .build();

    @BeforeEach
    void setup() {
        chatroomDefaultCoverRepository.deleteAll();
        fileRepository.deleteAll();
        file = fileRepository.save(file);
    }

    @Nested
    @DisplayName("save 메서드는")
    class Save {
        @Nested
        @DisplayName("호출 후 findById를")
        class findById {
            @Test
            @DisplayName("수행한 결과와 save한 entity는 동일하다.")
            void saveAndFindCompare() {
                //given
                ChatroomDefaultCover cover = ChatroomDefaultCover.builder()
                    .coverFile(file)
                    .build();

                cover = chatroomDefaultCoverRepository.save(cover);
                //when
                ChatroomDefaultCover result = chatroomDefaultCoverRepository.findById(cover.getId())
                    .orElseThrow(RuntimeException::new);

                //then
                assertThat(result).usingRecursiveComparison().isEqualTo(cover);
            }
        }
    }

}