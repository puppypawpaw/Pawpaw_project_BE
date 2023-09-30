package kr.co.pawpaw.api.service.chatroom;

import kr.co.pawpaw.api.dto.chatroom.ChatroomDetailResponse;
import kr.co.pawpaw.api.dto.chatroom.CreateChatroomRequest;
import kr.co.pawpaw.api.dto.chatroom.CreateChatroomResponse;
import kr.co.pawpaw.api.dto.chatroom.CreateChatroomWithDefaultCoverRequest;
import kr.co.pawpaw.api.service.file.FileService;
import kr.co.pawpaw.api.service.user.UserService;
import kr.co.pawpaw.common.exception.chatroom.AlreadyChatroomParticipantException;
import kr.co.pawpaw.common.exception.chatroom.IsNotChatroomParticipantException;
import kr.co.pawpaw.common.exception.chatroom.NotAllowedChatroomLeaveException;
import kr.co.pawpaw.common.exception.chatroom.NotFoundChatroomDefaultCoverException;
import kr.co.pawpaw.common.exception.user.NotFoundUserException;
import kr.co.pawpaw.domainrdb.chatroom.domain.*;
import kr.co.pawpaw.domainrdb.chatroom.dto.*;
import kr.co.pawpaw.domainrdb.chatroom.service.command.ChatroomCommand;
import kr.co.pawpaw.domainrdb.chatroom.service.command.ChatroomParticipantCommand;
import kr.co.pawpaw.domainrdb.chatroom.service.command.TrendingChatroomCommand;
import kr.co.pawpaw.domainrdb.chatroom.service.query.ChatroomDefaultCoverQuery;
import kr.co.pawpaw.domainrdb.chatroom.service.query.ChatroomParticipantQuery;
import kr.co.pawpaw.domainrdb.chatroom.service.query.ChatroomQuery;
import kr.co.pawpaw.domainrdb.chatroom.service.query.TrendingChatroomQuery;
import kr.co.pawpaw.domainrdb.storage.domain.File;
import kr.co.pawpaw.domainrdb.user.domain.User;
import kr.co.pawpaw.domainrdb.user.domain.UserId;
import kr.co.pawpaw.domainrdb.user.service.query.UserQuery;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.SliceImpl;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChatroomServiceTest {
    @Mock
    private ChatroomCommand chatroomCommand;
    @Mock
    private ChatroomParticipantCommand chatroomParticipantCommand;
    @Mock
    private ChatroomParticipantQuery chatroomParticipantQuery;
    @Mock
    private ChatroomDefaultCoverQuery chatroomDefaultCoverQuery;
    @Mock
    private TrendingChatroomCommand trendingChatroomCommand;
    @Mock
    private TrendingChatroomQuery trendingChatroomQuery;
    @Mock
    private ChatroomQuery chatroomQuery;
    @Mock
    private UserQuery userQuery;
    @Mock
    private FileService fileService;
    @Mock
    private UserService userService;
    @Mock
    private MultipartFile multipartFile;
    @InjectMocks
    private ChatroomService chatroomService;

    private final User user = User.builder().build();
    private final CreateChatroomRequest request = CreateChatroomRequest.builder()
        .name("chatroom-name")
        .description("chatroom-description")
        .locationLimit(true)
        .searchable(false)
        .hashTagList(List.of("hashTag1", "hashTag2"))
        .build();

    private final CreateChatroomWithDefaultCoverRequest requestWithDefaultCover = CreateChatroomWithDefaultCoverRequest.builder()
        .name("chatroom-name")
        .description("chatroom-description")
        .locationLimit(true)
        .searchable(false)
        .hashTagList(List.of("hashTag1", "hashTag2"))
        .coverId(12345L)
        .build();

    private final File file = File.builder()
        .fileName(UUID.randomUUID().toString())
        .uploader(user)
        .byteSize(123L)
        .contentType("contentType")
        .build();

    private final ChatroomDefaultCover cover = ChatroomDefaultCover.builder()
        .coverFile(file)
        .build();

    private static final Chatroom chatroom = Chatroom.builder()
        .build();

    @BeforeAll
    static void setup() throws NoSuchFieldException, IllegalAccessException {
        Field idField = Chatroom.class.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(chatroom, 12345L);
    }

    @Nested
    @DisplayName("createChatroom 메서드는")
    class CreateChatroom {
        @Nested
        @DisplayName("유저가")
        class User {
            @Test
            @DisplayName("존재하지않으면 예외가 발생한다.")
            void NotFoundUserException() {
                //given
                when(userQuery.findByUserId(user.getUserId())).thenReturn(Optional.empty());

                //when
                assertThatThrownBy(() -> chatroomService.createChatroom(user.getUserId(), request, multipartFile)).isInstanceOf(NotFoundUserException.class);

                //then
                verify(userQuery).findByUserId(user.getUserId());
            }

            @Nested
            @DisplayName("존재하고 생성된 chatroom의")
            class Chatroom {
                @Nested
                @DisplayName("mulitparFile이")
                class MultipartFile {
                    @Test
                    @DisplayName("null이면 fileService의 saveFileByMultipartFile메서드를 호출하지 않는다.")
                    void nullNotCallSaveFileByMultipartFile() {
                        //given
                        kr.co.pawpaw.domainrdb.chatroom.domain.Chatroom chatroom = request.toChatroom(file);
                        when(userQuery.findByUserId(user.getUserId())).thenReturn(Optional.of(user));
                        when(chatroomCommand.save(any(kr.co.pawpaw.domainrdb.chatroom.domain.Chatroom.class))).thenReturn(chatroom);
                        //when
                        chatroomService.createChatroom(user.getUserId(), request, null);
                        //then
                        verify(fileService, times(0)).saveFileByMultipartFile(any(), any());
                    }

                    @Test
                    @DisplayName("길이가 0이면 fileService의 saveFileByMultipartFile메서드를 호출하지 않는다.")
                    void zeroLenNotCallSaveFileByMultipartFile() throws IOException {
                        //given
                        kr.co.pawpaw.domainrdb.chatroom.domain.Chatroom chatroom = request.toChatroom(file);
                        when(userQuery.findByUserId(user.getUserId())).thenReturn(Optional.of(user));
                        when(chatroomCommand.save(any(kr.co.pawpaw.domainrdb.chatroom.domain.Chatroom.class))).thenReturn(chatroom);
                        when(multipartFile.getBytes()).thenReturn(new byte[0]);

                        //when
                        chatroomService.createChatroom(user.getUserId(), request, multipartFile);

                        //then
                        verify(fileService, times(0)).saveFileByMultipartFile(any(), any());
                    }

                    @Test
                    @DisplayName("정상이면 생성된 chatroom의 Id를 필드로 가지는 CreateChatroomResponse를 반환한다.")
                    void returnCreateChatroomResponse() throws IllegalAccessException, IOException, NoSuchFieldException {
                        //given
                        kr.co.pawpaw.domainrdb.chatroom.domain.Chatroom chatroom = request.toChatroom(file);
                        Field idField = chatroom.getClass().getDeclaredField("id");
                        idField.setAccessible(true);
                        Long id = 1234L;
                        idField.set(chatroom, id);
                        when(userQuery.findByUserId(user.getUserId())).thenReturn(Optional.of(user));
                        when(chatroomCommand.save(any(kr.co.pawpaw.domainrdb.chatroom.domain.Chatroom.class))).thenReturn(chatroom);
                        when(multipartFile.getBytes()).thenReturn(new byte[file.getByteSize().intValue()]);
                        when(fileService.saveFileByMultipartFile(multipartFile, user.getUserId())).thenReturn(file);
                        //when
                        CreateChatroomResponse response = chatroomService.createChatroom(user.getUserId(), request, multipartFile);

                        //then
                        assertThat(response.getChatroomId()).isEqualTo(id);
                    }
                }
            }
        }
    }

    @Nested
    @DisplayName("createChatroomWithDefaultCover 메서드는")
    class CreateChatroomWithDefaultCover {
        @Nested
        @DisplayName("유저가")
        class User {
            @Test
            @DisplayName("존재하지않으면 예외가 발생한다.")
            void NotFoundUserException() {
                //given
                when(userQuery.findByUserId(user.getUserId())).thenReturn(Optional.empty());

                //when
                assertThatThrownBy(() -> chatroomService.createChatroomWithDefaultCover(user.getUserId(), requestWithDefaultCover)).isInstanceOf(NotFoundUserException.class);

                //then
                verify(userQuery).findByUserId(user.getUserId());
            }

            @Nested
            @DisplayName("존재하고 ChatroomDefaultCover가")
            class ChatroomDefaultCover {
                @Test
                @DisplayName("존재하지 않으면 예외가 발생한다.")
                void notFoundChatroomDefaultCoverException() {
                    //given
                    Long chatroomDefaultCoverId = 12345L;
                    when(userQuery.findByUserId(user.getUserId())).thenReturn(Optional.of(user));
                    when(chatroomDefaultCoverQuery.findById(chatroomDefaultCoverId)).thenReturn(Optional.empty());

                    //when
                    assertThatThrownBy(() -> chatroomService.createChatroomWithDefaultCover(user.getUserId(), requestWithDefaultCover)).isInstanceOf(NotFoundChatroomDefaultCoverException.class);

                    //then
                }

                @Test
                @DisplayName("존재하면 생성된 chatroom의 Id를 필드로 가지는 CreateChatroomResponse를 반환한다.")
                void returnCreateChatroomResponse() throws NoSuchFieldException, IllegalAccessException {
                    //given
                    kr.co.pawpaw.domainrdb.chatroom.domain.Chatroom chatroom = requestWithDefaultCover.toChatroom(file);
                    Field idField = chatroom.getClass().getDeclaredField("id");
                    idField.setAccessible(true);
                    idField.set(chatroom, requestWithDefaultCover.getCoverId());
                    when(userQuery.findByUserId(user.getUserId())).thenReturn(Optional.of(user));
                    when(chatroomDefaultCoverQuery.findById(requestWithDefaultCover.getCoverId())).thenReturn(Optional.of(cover));
                    when(chatroomCommand.save(any(kr.co.pawpaw.domainrdb.chatroom.domain.Chatroom.class))).thenReturn(chatroom);

                    //when
                    CreateChatroomResponse response = chatroomService.createChatroomWithDefaultCover(user.getUserId(), requestWithDefaultCover);

                    //then
                    assertThat(response.getChatroomId()).isEqualTo(requestWithDefaultCover.getCoverId());
                }
            }
        }
    }

    @Nested
    @DisplayName("getChatroomDefaultCoverList 메서드는")
    class GetChatroomDefaultCoverList {
        @Nested
        @DisplayName("chatroomDefaultCoverQuery의")
        class ChatroomDefaultCoverQuery {
            @Test
            @DisplayName("findAllChatroomCover 메서드를 호출하고 반환값을 그대로 반환한다.")
            void findAllChatroomCover() {
                //given
                List<ChatroomCoverResponse> responseList = List.of(
                    new ChatroomCoverResponse(12345L, "coverUrl1"),
                    new ChatroomCoverResponse(123456L, "coverUrl2")
                );
                when(chatroomDefaultCoverQuery.findAllChatroomCover()).thenReturn(responseList);

                //when
                List<ChatroomCoverResponse> result = chatroomService.getChatroomDefaultCoverList();

                //then
                assertThat(result).usingRecursiveComparison().isEqualTo(responseList);
                verify(chatroomDefaultCoverQuery).findAllChatroomCover();
            }
        }
    }

    @Nested
    @DisplayName("joinChatroom 메서드는")
    class JoinChatroom {
        @Nested
        @DisplayName("유저가")
        class User {
            @Test
            @DisplayName("존재하지 않으면 예외가 발생한다.")
            void NotFoundUserException() {
                //given
                when(userQuery.findByUserId(user.getUserId())).thenReturn(Optional.empty());

                //when
                assertThatThrownBy(() -> chatroomService.joinChatroom(user.getUserId(), chatroom.getId())).isInstanceOf(NotFoundUserException.class);

                //then
            }
        }
        @Nested
        @DisplayName("채팅방에")
        class Chatroom {
            @Test
            @DisplayName("이미 참여했으면 예외가 발생한다.")
            void alreadyChatroomParticipantException() {
                //given
                when(userQuery.findByUserId(user.getUserId())).thenReturn(Optional.of(user));
                when(chatroomParticipantQuery.existsByUserIdAndChatroomId(user.getUserId(), chatroom.getId())).thenReturn(true);
                //when
                assertThatThrownBy(() -> chatroomService.joinChatroom(user.getUserId(), chatroom.getId())).isInstanceOf(AlreadyChatroomParticipantException.class);

                //then
            }

            @Test
            @DisplayName("참여하지 않았으면 participant role로 참여자를 생성한다.")
            void success() {
                //given
                when(userQuery.findByUserId(user.getUserId())).thenReturn(Optional.of(user));
                when(chatroomParticipantQuery.existsByUserIdAndChatroomId(user.getUserId(), chatroom.getId())).thenReturn(false);
                when(trendingChatroomQuery.existsByChatroomId(chatroom.getId())).thenReturn(true);
                //when
                chatroomService.joinChatroom(user.getUserId(), chatroom.getId());

                //then
                ArgumentCaptor<ChatroomParticipant> chatroomParticipantArgumentCaptor = ArgumentCaptor.forClass(ChatroomParticipant.class);
                verify(chatroomParticipantCommand).save(chatroomParticipantArgumentCaptor.capture());
                assertThat(chatroomParticipantArgumentCaptor.getValue().isManager()).isFalse();
            }
        }

        @Nested
        @DisplayName("참여한 채팅방을")
        class ParticipatedChatroom {
            @Test
            @DisplayName("뜨고있는 채팅방으로 설정한다.")
            void setTrendingChatroom() throws NoSuchFieldException, IllegalAccessException {
                //given
                when(userQuery.findByUserId(user.getUserId())).thenReturn(Optional.of(user));
                when(chatroomParticipantQuery.existsByUserIdAndChatroomId(user.getUserId(), chatroom.getId())).thenReturn(false);
                when(trendingChatroomQuery.existsByChatroomId(chatroom.getId())).thenReturn(false);
                when(chatroomQuery.getReferenceById(chatroom.getId())).thenReturn(chatroom);

                //when
                chatroomService.joinChatroom(user.getUserId(), chatroom.getId());

                //then
                ArgumentCaptor<TrendingChatroom> trendingChatroomArgumentCaptor = ArgumentCaptor.forClass(TrendingChatroom.class);
                verify(trendingChatroomCommand).save(trendingChatroomArgumentCaptor.capture());
                assertThat(trendingChatroomArgumentCaptor.getValue().getChatroom().getId()).isEqualTo(chatroom.getId());
            }
        }
    }

    @Test
    @DisplayName("leaveChatroom 메서드는 존재하지 않는 유저 예외가 발생 가능하다.")
    void leaveChatroomNotFoundUserException() {
        //given
        when(userQuery.findByUserId(user.getUserId())).thenReturn(Optional.empty());
        //when
        assertThatThrownBy(() -> chatroomService.leaveChatroom(user.getUserId(), chatroom.getId())).isInstanceOf(NotFoundUserException.class);

        //then
    }

    @Test
    @DisplayName("leaveChatroom 메서드는 채팅방 참여자가 아니라는 예외가 발생할 수 있다.")
    void leaveChatroomIsNotChatroomParticipantException() {
        //given
        when(userQuery.findByUserId(user.getUserId())).thenReturn(Optional.of(user));
        when(chatroomParticipantQuery.findByUserIdAndChatroomId(user.getUserId(), chatroom.getId())).thenReturn(Optional.empty());

        //when
        assertThatThrownBy(() -> chatroomService.leaveChatroom(user.getUserId(), chatroom.getId())).isInstanceOf(IsNotChatroomParticipantException.class);

        //then
    }

    @Test
    @DisplayName("leaveChatroom 메서드는 매니저는 호출 불가능하다.")
    void leaveChatroomNotAllowedChatroomLeaveException() {
        //given
        when(userQuery.findByUserId(user.getUserId())).thenReturn(Optional.of(user));
        when(chatroomParticipantQuery.findByUserIdAndChatroomId(user.getUserId(), chatroom.getId())).thenReturn(Optional.of(ChatroomParticipant.builder()
                .role(ChatroomParticipantRole.MANAGER)
            .build()));

        //when
        assertThatThrownBy(() -> chatroomService.leaveChatroom(user.getUserId(), chatroom.getId())).isInstanceOf(NotAllowedChatroomLeaveException.class);

        //then
    }

    @Test
    @DisplayName("leaveChatroom 메서드는 chatroomParticipantCommand의 delete메서드를 호출한다.")
    void leaveChatroom() {
        //given
        when(userQuery.findByUserId(user.getUserId())).thenReturn(Optional.of(user));
        ChatroomParticipant chatroomParticipant = ChatroomParticipant.builder()
            .role(ChatroomParticipantRole.PARTICIPANT)
            .build();
        when(chatroomParticipantQuery.findByUserIdAndChatroomId(user.getUserId(), chatroom.getId())).thenReturn(Optional.of(chatroomParticipant));

        //when
        chatroomService.leaveChatroom(user.getUserId(), chatroom.getId());

        //then
        verify(chatroomParticipantCommand).delete(chatroomParticipant);
    }

    @Test
    @DisplayName("getParticipatedChatroomList 메서드는 chatroomQuery의 getParticipatedChatroomDetailDataByUserId 메서드를 호출한다.")
    void getParticipatedChatroomList() {
        //given
        UserId userId = UserId.create();

        List<ChatroomDetailData> chatroomDetailData = List.of(
            new ChatroomDetailData(
                1L,
                "name",
                "description",
                "coverUrl",
                LocalDateTime.now().minusDays(1),
                List.of("hashTag1", "hashTag2"),
                "managerName",
                "managerImageUrl",
                2L,
                false,
                false
            )
        );

        List<ChatroomDetailResponse> resultExpected = chatroomDetailData.stream()
            .map(ChatroomDetailResponse::of)
            .collect(Collectors.toList());

        when(chatroomQuery.getParticipatedChatroomDetailDataByUserId(userId)).thenReturn(chatroomDetailData);

        //when
        List<ChatroomDetailResponse> result = chatroomService.getParticipatedChatroomList(userId);

        //then
        assertThat(result).usingRecursiveComparison().isEqualTo(resultExpected);
        verify(chatroomQuery).getParticipatedChatroomDetailDataByUserId(userId);
    }

    @Test
    @DisplayName("getRecommendedNewChatroomList 메서드는 chatroomQuery의 getAccessibleNewChatroomByUserId 메서드를 호출함")
    void getRecommendedChatroomList() {
        //given
        UserId userId = UserId.create();

        when(chatroomQuery.getAccessibleNewChatroomByUserId(userId)).thenReturn(List.of(new ChatroomResponse(
            1L,
            "name",
            "description",
            List.of("hashTag1"),
            "managerName",
            "managerImageUrl",
            2L
        )));

        //when
        chatroomService.getRecommendedNewChatroomList(userId);

        //then
        verify(chatroomQuery).getAccessibleNewChatroomByUserId(userId);
    }

    @Test
    @DisplayName("getTrendingChatroomList 메서드는 chatroomQuery의 getAccessibleTrendingChatroom를 호출한다.")
    void getTrendingChatroomList() {
        //given
        UserId userId = UserId.create();
        Long beforeId = null;
        int size = 10;

        when(chatroomQuery.getAccessibleTrendingChatroom(userId, beforeId, size)).thenReturn(new SliceImpl<>(
            List.of(), PageRequest.of(0, 10),  false
        ));

        //when
        chatroomService.getTrendingChatroomList(userId, beforeId, size);

        //then
        verify(chatroomQuery).getAccessibleTrendingChatroom(userId, beforeId, size);
    }

    @Nested
    @DisplayName("getChatroomParticipantResponseList 메서드는")
    class GetChatroomParticipantResponseList {
        String defaultImageUrl = "기본 이미지 URL";
        Long chatroomId = 12345L;
        List<ChatroomParticipantResponse> nullResponseList = List.of(
            new ChatroomParticipantResponse(
                "nickname",
                "briefIntroduction",
                null,
                ChatroomParticipantRole.PARTICIPANT
            )
        );
        List<ChatroomParticipantResponse> nonNullResponseList = nullResponseList.stream()
            .map(response -> new ChatroomParticipantResponse(
                response.getNickname(),
                response.getBriefIntroduction(),
                defaultImageUrl,
                response.getRole()
            )).collect(Collectors.toList());

        @Test
        @DisplayName("ChatroomParticipantResponse의 ImageUrl이 null이면 기본 이미지 URL로 변경한다.")
        void changeDefaultImageUrl() {
            //given
            when(userService.getUserDefaultImageUrl()).thenReturn(defaultImageUrl);
            when(chatroomParticipantQuery.getChatroomParticipantResponseList(chatroomId)).thenReturn(nullResponseList);

            //when
            List<ChatroomParticipantResponse> result = chatroomService.getChatroomParticipantResponseList(chatroomId);

            //then
            assertThat(nonNullResponseList).usingRecursiveComparison().isEqualTo(result);
        }
    }

    @Nested
    @DisplayName("searchChatroomNonParticipants 메서드는")
    class SearchChatroomNonParticipants {
        String defaultImageUrl = "기본 이미지 URL";
        Long chatroomId = 1234L;
        String nicknameKeyword = "nickname";
        List<ChatroomNonParticipantResponse> nullResponseList = List.of(
            new ChatroomNonParticipantResponse(
                "nickname",
                "briefIntroduction",
                null
            )
        );

        List<ChatroomNonParticipantResponse> nonNullResponseList = nullResponseList
            .stream()
            .map(response -> new ChatroomNonParticipantResponse(
                response.getNickname(),
                response.getBriefIntroduction(),
                defaultImageUrl
            )).collect(Collectors.toList());

        @Test
        @DisplayName("ChatroomNonParticipantResponse의 ImageUrl이 null이면 기본 이미지 URL로 변경한다.")
        void changeDefaultImageUrl() {
            //given
            when(userService.getUserDefaultImageUrl()).thenReturn(defaultImageUrl);
            when(userQuery.searchChatroomNonParticipant(chatroomId, nicknameKeyword)).thenReturn(nullResponseList);


            //when
            List<ChatroomNonParticipantResponse> result = chatroomService.searchChatroomNonParticipants(chatroomId, nicknameKeyword);

            //then
            assertThat(result).usingRecursiveComparison().isEqualTo(nonNullResponseList);
        }
    }
}