package kr.co.pawpaw.domainrdb.chatroom.repository;

import kr.co.pawpaw.domainrdb.chatroom.domain.Chatroom;
import kr.co.pawpaw.domainrdb.chatroom.domain.ChatroomSchedule;
import kr.co.pawpaw.domainrdb.chatroom.domain.ChatroomScheduleParticipant;
import kr.co.pawpaw.domainrdb.chatroom.dto.ChatroomScheduleData;
import kr.co.pawpaw.domainrdb.chatroom.dto.ChatroomScheduleParticipantResponse;
import kr.co.pawpaw.domainrdb.config.QuerydslConfig;
import kr.co.pawpaw.domainrdb.storage.domain.File;
import kr.co.pawpaw.domainrdb.storage.repository.FileRepository;
import kr.co.pawpaw.domainrdb.user.domain.User;
import kr.co.pawpaw.domainrdb.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;

@Slf4j
@Import(value = { QuerydslConfig.class, ChatroomScheduleCustomRepository.class })
@DataJpaTest
class ChatroomScheduleCustomRepositoryTest {
    @Autowired
    private ChatroomScheduleCustomRepository chatroomScheduleCustomRepository;
    @Autowired
    private ChatroomRepository chatroomRepository;
    @Autowired
    private ChatroomScheduleRepository chatroomScheduleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ChatroomScheduleParticipantRepository chatroomScheduleParticipantRepository;
    @Autowired
    private FileRepository fileRepository;

    User creator;
    File creatorImageFile;
    Chatroom chatroom1;
    Chatroom chatroom2;
    ChatroomSchedule notEndScheduleFor2;
    ChatroomSchedule notEndScheduleFor1;
    ChatroomSchedule endScheduleFor1;
    ChatroomScheduleParticipant chatroomScheduleParticipantFor1;

    @BeforeEach
    void setup() {
        creator = userRepository.save(User.builder()
            .build());

        creatorImageFile = fileRepository.save(File.builder()
                .fileName("creatorImageFile-fileName")
                .fileUrl("creatorImageFile-fileUrl")
                .uploader(creator)
                .byteSize(123L)
                .contentType("creatorImageFile-contentType")
            .build());

        creator.updateImage(creatorImageFile);
        creator = userRepository.save(creator);
        chatroom1 = chatroomRepository.save(Chatroom.builder()
                .name("chatroom1-name")
                .description("chatroom1-description")
                .searchable(true)
                .locationLimit(true)
            .build());

        chatroom2 = chatroomRepository.save(Chatroom.builder()
            .name("chatroom2-name")
            .description("chatroom2-description")
            .searchable(true)
            .locationLimit(true)
            .build());

        notEndScheduleFor1 = chatroomScheduleRepository.save(ChatroomSchedule.builder()
            .name("notEndScheduleFor1-name")
            .description("notEndScheduleFor1-description")
            .startDate(LocalDateTime.of(2023,9,16,15,45,0))
            .endDate(LocalDateTime.of(3333,9,16,15,45,0))
            .chatroom(chatroom1)
            .creator(creator)
            .build());

        endScheduleFor1 = chatroomScheduleRepository.save(ChatroomSchedule.builder()
            .name("endScheduleFor1-name")
            .description("endScheduleFor1-description")
            .startDate(LocalDateTime.of(2023,9,16,15,44,0))
            .endDate(LocalDateTime.of(2023,9,16,15,45,0))
            .chatroom(chatroom1)
            .creator(creator)
            .build());

        notEndScheduleFor2 = chatroomScheduleRepository.save(ChatroomSchedule.builder()
            .name("notEndScheduleFor2-name")
            .description("notEndScheduleFor2-description")
            .startDate(LocalDateTime.of(2023,9,16,15,45,0))
            .endDate(LocalDateTime.of(3333,9,16,15,45,0))
            .chatroom(chatroom2)
            .creator(creator)
            .build());

        chatroomScheduleParticipantFor1 = chatroomScheduleParticipantRepository.save(ChatroomScheduleParticipant.builder()
            .chatroomSchedule(notEndScheduleFor1)
            .user(creator)
            .build());
    }

    @Test
    @DisplayName("findNotEndChatroomScheduleByChatroomId 메서드는 채팅방에 속한 스케줄만 가져온다.")
    void findNotEndChatroomScheduleByChatroomIdChatroomIdCheck() {
        //given

        //when
        List<ChatroomScheduleData> result = chatroomScheduleCustomRepository.findNotEndChatroomScheduleByChatroomId(chatroom2.getId());

        //then
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0).getId()).isEqualTo(notEndScheduleFor2.getId());
    }

    @Test
    @DisplayName("findNotEndChatroomScheduleByChatroomId 메서드는 종료 시간 이후의 채팅방 스케줄은 가져오지 않는다.")
    void findNotEndChatroomScheduleByChatroomIdNotEnd() {
        //given

        //when
        List<ChatroomScheduleData> result = chatroomScheduleCustomRepository.findNotEndChatroomScheduleByChatroomId(chatroom1.getId());

        //then
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0).getId()).isEqualTo(notEndScheduleFor1.getId());
    }

    @Test
    @DisplayName("findNotEndChatroomScheduleByChatroomId 메서드는 참여자가 없으면 emptyCollection으로 참여자 목록을 반환한다.")
    void findNotEndChatroomScheduleByChatroomIdParticipantEmptyList() {
        //given

        //when
        List<ChatroomScheduleData> result1 = chatroomScheduleCustomRepository.findNotEndChatroomScheduleByChatroomId(chatroom1.getId());
        List<ChatroomScheduleData> result2 = chatroomScheduleCustomRepository.findNotEndChatroomScheduleByChatroomId(chatroom2.getId());

        //then
        assertThat(result1.size()).isEqualTo(1);
        assertThat(result1.get(0).getParticipants().size()).isEqualTo(1);
        assertThat(result2.size()).isEqualTo(1);
        assertThat(result2.get(0).getParticipants().size()).isEqualTo(0);
    }

    @Test
    @DisplayName("findNotEndChatroomScheduleByChatroomId 메서드 반환값 테스트")
    void findNotEndChatroomScheduleByChatroomIdReturnValue() {
        //given
        ChatroomScheduleData resultExpected = new ChatroomScheduleData(
            notEndScheduleFor1.getId(),
            notEndScheduleFor1.getName(),
            notEndScheduleFor1.getDescription(),
            notEndScheduleFor1.getStartDate(),
            notEndScheduleFor1.getEndDate(),
            Set.of(new ChatroomScheduleParticipantResponse(
                chatroomScheduleParticipantFor1.getUser().getNickname(),
                chatroomScheduleParticipantFor1.getUser().getUserImage().getFileUrl()
            ))
        );

        //when
        List<ChatroomScheduleData> result = chatroomScheduleCustomRepository.findNotEndChatroomScheduleByChatroomId(chatroom1.getId());

        //then
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0)).usingRecursiveComparison().isEqualTo(resultExpected);
    }
}