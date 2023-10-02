package kr.co.pawpaw.mysql.chatroom.repository;

import kr.co.pawpaw.mysql.chatroom.domain.ChatroomDefaultCover;
import kr.co.pawpaw.mysql.chatroom.dto.ChatroomCoverResponse;
import kr.co.pawpaw.mysql.config.QuerydslConfig;
import kr.co.pawpaw.mysql.storage.domain.File;
import kr.co.pawpaw.mysql.storage.repository.FileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@Import(value = {QuerydslConfig.class, ChatroomDefaultCoverCustomRepository.class})
@DataJpaTest
@Nested
@DisplayName("ChatroomDefaultCoverCustomRepository 는")
class ChatroomDefaultCoverCustomRepositoryTest {
    @Autowired
    private ChatroomDefaultCoverRepository chatroomDefaultCoverRepository;
    @Autowired
    private FileRepository fileRepository;
    @Autowired
    private ChatroomDefaultCoverCustomRepository chatroomDefaultCoverCustomRepository;

    private List<File> fileList = List.of(
        File.builder()
            .fileName("fileName1")
            .fileUrl("fileUrl1")
            .byteSize(123L)
            .contentType("contentType")
            .build(),
        File.builder()
            .fileName("fileName2")
            .fileUrl("fileUrl2")
            .byteSize(123L)
            .contentType("contentType")
            .build()
    );

    private List<ChatroomDefaultCover> chatroomDefaultCoverList;

    @BeforeEach
    void setup() {
        chatroomDefaultCoverRepository.deleteAll();
        fileRepository.deleteAll();
        fileList = fileRepository.saveAll(fileList);
        chatroomDefaultCoverList = chatroomDefaultCoverRepository.saveAll(List.of(
            ChatroomDefaultCover.builder()
                .coverFile(fileList.get(0))
                .build(),
            ChatroomDefaultCover.builder()
                .coverFile(fileList.get(1))
                .build()
        ));
    }

    @Nested
    @DisplayName("findAllChatroomCover 메서드는 ")
    class findAllChatroomCover {
        @Test
        @DisplayName("호출시 저장된 모든 chatroomDefaultCover에 해당하는 ChatroomCoverResponse List를 반환한다.")
        void call() {
            //given
            List<ChatroomCoverResponse> resultExpected = chatroomDefaultCoverList.stream()
                .map(cover -> new ChatroomCoverResponse(cover.getId(), cover.getCoverFile().getFileUrl()))
                .collect(Collectors.toList());

            //when
            List<ChatroomCoverResponse> result = chatroomDefaultCoverCustomRepository.findAllChatroomCover();

            //then
            assertThat(result).usingRecursiveComparison().isEqualTo(resultExpected);
        }
    }
}