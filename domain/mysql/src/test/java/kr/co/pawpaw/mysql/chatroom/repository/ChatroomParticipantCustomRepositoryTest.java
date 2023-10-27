package kr.co.pawpaw.mysql.chatroom.repository;

import kr.co.pawpaw.mysql.chatroom.domain.Chatroom;
import kr.co.pawpaw.mysql.chatroom.domain.ChatroomParticipant;
import kr.co.pawpaw.mysql.chatroom.domain.ChatroomParticipantRole;
import kr.co.pawpaw.mysql.chatroom.dto.ChatroomParticipantResponse;
import kr.co.pawpaw.mysql.common.MySQLTestContainer;
import kr.co.pawpaw.mysql.config.QuerydslConfig;
import kr.co.pawpaw.mysql.position.Position;
import kr.co.pawpaw.mysql.storage.domain.File;
import kr.co.pawpaw.mysql.storage.repository.FileRepository;
import kr.co.pawpaw.mysql.user.domain.User;
import kr.co.pawpaw.mysql.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@Import(value = { QuerydslConfig.class, ChatroomParticipantCustomRepository.class })
@Nested
@DisplayName("ChatroomParticipantCustomRepository 클래스의")
class ChatroomParticipantCustomRepositoryTest extends MySQLTestContainer {
    @Autowired
    private ChatroomParticipantCustomRepository chatroomParticipantCustomRepository;
    @Autowired
    private ChatroomParticipantRepository chatroomParticipantRepository;
    @Autowired
    private ChatroomRepository chatroomRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FileRepository fileRepository;

    @Nested
    @DisplayName("getChatroomParticipantResponseList 메서드는")
    class GetChatroomParticipantResponseList {
        List<User> userList;
        List<ChatroomParticipant> chatroomParticipantList;
        Chatroom chatroom;
        @BeforeEach
        void setup() {
            chatroomParticipantRepository.deleteAll();
            userRepository.deleteAll();
            fileRepository.deleteAll();
            chatroomRepository.deleteAll();

            File user1ImageFile = fileRepository.save(
                File.builder()
                    .fileName("user1-image")
                    .fileUrl("user1-image-url")
                    .byteSize(1234L)
                    .contentType("image/jpeg")
                    .build()
            );
            userList = userRepository.saveAll(List.of(
                User.builder()
                    .email("user1-email")
                    .password("user1-password")
                    .name("user1-name")
                    .nickname("user1-nickname")
                    .position(Position.builder()
                        .name("user1-position")
                        .latitude(12.3)
                        .longitude(12.4)
                        .build())
                    .userImage(user1ImageFile)
                    .build(),
                User.builder()
                    .email("user2-email")
                    .password("user2-password")
                    .name("user2-name")
                    .nickname("user2-nickname")
                    .position(Position.builder()
                        .name("user2-position")
                        .latitude(32.1)
                        .longitude(32.2)
                        .build())
                    .build()
            ));

            chatroom = chatroomRepository.save(
                Chatroom.builder()
                    .name("chatroom1-name")
                    .description("chatroom1-description")
                    .locationLimit(false)
                    .searchable(true)
                    .build()
            );

            chatroomParticipantList = chatroomParticipantRepository.saveAll(
                List.of(
                    ChatroomParticipant.builder()
                        .chatroom(chatroom)
                        .role(ChatroomParticipantRole.MANAGER)
                        .user(userList.get(0))
                        .build(),
                    ChatroomParticipant.builder()
                        .chatroom(chatroom)
                        .role(ChatroomParticipantRole.PARTICIPANT)
                        .user(userList.get(1))
                        .build()
                )
            );
        }

        @Test
        @DisplayName("채팅방 참가자의 정보를 반환하고 유저의 이미지가 없으면 null을 있으면 이미지 url을 ChatroomParticipantResponse의 imageUrl 필드에 넣어서 반환한다.")
        void returnParticipantInfo() {
            //given
            List<ChatroomParticipantResponse> expectedResult = chatroomParticipantList
                .stream()
                .map(participant -> new ChatroomParticipantResponse(
                    participant.getUser().getUserId(),
                    participant.getUser().getNickname(),
                    participant.getUser().getBriefIntroduction(),
                    Objects.nonNull(participant.getUser().getUserImage()) ?
                        participant.getUser().getUserImage().getFileUrl() :
                        null,
                    participant.getRole()
                )).collect(Collectors.toList());

            //when
            List<ChatroomParticipantResponse> result = chatroomParticipantCustomRepository.getChatroomParticipantResponseList(chatroom.getId());

            //then
            assertThat(result).usingRecursiveComparison().isEqualTo(expectedResult);
        }
    }
}