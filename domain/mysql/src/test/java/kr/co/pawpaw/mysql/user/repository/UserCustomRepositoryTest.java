package kr.co.pawpaw.mysql.user.repository;

import kr.co.pawpaw.mysql.chatroom.domain.Chatroom;
import kr.co.pawpaw.mysql.chatroom.domain.ChatroomParticipant;
import kr.co.pawpaw.mysql.chatroom.domain.ChatroomParticipantRole;
import kr.co.pawpaw.mysql.chatroom.dto.ChatroomNonParticipantResponse;
import kr.co.pawpaw.mysql.chatroom.repository.ChatroomParticipantRepository;
import kr.co.pawpaw.mysql.chatroom.repository.ChatroomRepository;
import kr.co.pawpaw.mysql.common.MySQLTestContainer;
import kr.co.pawpaw.mysql.config.QuerydslConfig;
import kr.co.pawpaw.mysql.position.Position;
import kr.co.pawpaw.mysql.storage.domain.File;
import kr.co.pawpaw.mysql.storage.repository.FileRepository;
import kr.co.pawpaw.mysql.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@Import(value = { QuerydslConfig.class, UserCustomRepository.class })
@Nested
@DisplayName("UserCustomRepository의")
class UserCustomRepositoryTest extends MySQLTestContainer {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ChatroomRepository chatroomRepository;
    @Autowired
    private ChatroomParticipantRepository chatroomParticipantRepository;
    @Autowired
    private FileRepository fileRepository;
    @Autowired
    private UserCustomRepository userCustomRepository;

    @Nested
    @DisplayName("searchChatroomNonParticipant 메서드는")
    class searchChatroomNonParticipant {
        File user1ImageFile;
        List<User> userList;
        Chatroom chatroom;
        List<ChatroomParticipant> chatroomParticipantList;

        @BeforeEach
        void setup() {
            userRepository.deleteAll();
            chatroomRepository.deleteAll();
            fileRepository.deleteAll();
            chatroomParticipantRepository.deleteAll();
            user1ImageFile = fileRepository.save(
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
                    .nickname("가")
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
                    .nickname("가나")
                    .position(Position.builder()
                        .name("user2-position")
                        .latitude(12.3)
                        .longitude(12.4)
                        .build())
                    .build(),
                User.builder()
                    .email("user3-email")
                    .password("user3-password")
                    .name("user3-name")
                    .nickname("가나다")
                    .position(Position.builder()
                        .name("user3-position")
                        .latitude(12.3)
                        .longitude(12.4)
                        .build())
                    .build(),
                User.builder()
                    .email("user4-email")
                    .password("user4-password")
                    .name("user4-name")
                    .nickname("가나다라")
                    .position(Position.builder()
                        .name("user4-position")
                        .latitude(12.3)
                        .longitude(12.4)
                        .build())
                    .build(),
                User.builder()
                    .email("user5-email")
                    .password("user5-password")
                    .name("user5-name")
                    .nickname("가나다라마")
                    .position(Position.builder()
                        .name("user5-position")
                        .latitude(12.3)
                        .longitude(12.4)
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
                        .user(userList.get(4))
                        .build()
                )
            );
        }

        @ParameterizedTest
        @CsvSource(value = {"0,4", "1,3", "2,2", "3,1", "4,0"})
        @DisplayName("채팅방 참여자가 아닌 유저들을 닉네임으로 검색한다.")
        void searchNotParticipantsByNickName(int userListIndex, int expectedSize) {
            //when
            List<ChatroomNonParticipantResponse> result = userCustomRepository.searchChatroomNonParticipant(chatroom.getId(), userList.get(userListIndex).getNickname());

            //then
            assertThat(result.size()).isEqualTo(expectedSize);
        }

        @ParameterizedTest
        @CsvSource(value = {"0,false", "1,true"})
        @DisplayName("ChatroomNonParticipantResponse 의 imageUrl 필드는 유저의 이미지가 있으면 이미지 url을 없으면 null을 반환한다.")
        void noImageReturnNullImageUrl(int userListIndex, boolean imageUrlIsNull) {
            //when
            List<ChatroomNonParticipantResponse> list = userCustomRepository.searchChatroomNonParticipant(chatroom.getId(), userList.get(userListIndex).getNickname());

            //then
            assertThat(
                list.stream().filter(response -> response.getUserId().equals(userList.get(userListIndex).getUserId())
                    && Objects.nonNull(response.getImageUrl())).findAny().isEmpty())
                .isEqualTo(imageUrlIsNull);
        }
    }
}