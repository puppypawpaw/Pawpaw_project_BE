package kr.co.pawpaw.api.service.chatroom;

import kr.co.pawpaw.api.dto.chatroom.ChatroomScheduleResponse;
import kr.co.pawpaw.api.dto.chatroom.CreateChatroomScheduleRequest;
import kr.co.pawpaw.api.dto.chatroom.CreateChatroomScheduleResponse;
import kr.co.pawpaw.api.service.user.UserService;
import kr.co.pawpaw.common.exception.chatroom.AlreadyChatroomScheduleParticipantException;
import kr.co.pawpaw.common.exception.chatroom.NotAChatroomScheduleParticipantException;
import kr.co.pawpaw.common.exception.chatroom.NotFoundChatroomScheduleException;
import kr.co.pawpaw.mysql.chatroom.domain.Chatroom;
import kr.co.pawpaw.mysql.chatroom.domain.ChatroomSchedule;
import kr.co.pawpaw.mysql.chatroom.domain.ChatroomScheduleParticipant;
import kr.co.pawpaw.mysql.chatroom.dto.ChatroomScheduleData;
import kr.co.pawpaw.mysql.chatroom.service.command.ChatroomScheduleCommand;
import kr.co.pawpaw.mysql.chatroom.service.command.ChatroomScheduleParticipantCommand;
import kr.co.pawpaw.mysql.chatroom.service.query.ChatroomQuery;
import kr.co.pawpaw.mysql.chatroom.service.query.ChatroomScheduleParticipantQuery;
import kr.co.pawpaw.mysql.chatroom.service.query.ChatroomScheduleQuery;
import kr.co.pawpaw.mysql.user.domain.User;
import kr.co.pawpaw.mysql.user.domain.UserId;
import kr.co.pawpaw.mysql.user.service.query.UserQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatroomScheduleService {
    private final ChatroomScheduleQuery chatroomScheduleQuery;
    private final ChatroomScheduleCommand chatroomScheduleCommand;
    private final ChatroomScheduleParticipantCommand chatroomScheduleParticipantCommand;
    private final ChatroomScheduleParticipantQuery chatroomScheduleParticipantQuery;
    private final UserService userService;
    private final UserQuery userQuery;
    private final ChatroomQuery chatroomQuery;

    @Transactional
    public CreateChatroomScheduleResponse createChatroomSchedule(
        final UserId userId,
        final Long chatroomId,
        final CreateChatroomScheduleRequest request
    ) {
        ChatroomSchedule chatroomSchedule = createChatroomSchedule(
            userQuery.getReferenceById(userId),
            chatroomQuery.getReferenceById(chatroomId),
            request
        );

        return CreateChatroomScheduleResponse.of(chatroomSchedule);
    }

    @Transactional
    public void participateChatroomSchedule(
        final UserId userId,
        final Long chatroomId,
        final Long chatroomScheduleId
    ) {
        checkChatroomSchedule(chatroomId, chatroomScheduleId);
        checkAlreadyParticipant(chatroomScheduleId, userId);
        createChatroomScheduleParticipant(userId, chatroomScheduleId);
    }

    @Transactional
    public void leaveChatroomSchedule(
        final UserId userId,
        final Long chatroomId,
        final Long chatroomScheduleId
    ) {
        checkChatroomSchedule(chatroomId, chatroomScheduleId);
        deleteChatroomScheduleParticipant(userId, chatroomScheduleId);
    }

    @Transactional(readOnly = true)
    public List<ChatroomScheduleResponse> getNotEndChatroomScheduleList(
        final UserId userId,
        final Long chatroomId
    ) {
        String userImageDefaultUrl = userService.getUserDefaultImageUrl();

        List<ChatroomScheduleData> scheduleDataList =
            chatroomScheduleQuery.findNotEndChatroomScheduleByChatroomId(chatroomId);

        changeNullImageUrlToUserDefaultImageUrl(scheduleDataList, userImageDefaultUrl);

        return scheduleDataList.stream()
            .map(ChatroomScheduleResponse::of)
            .collect(Collectors.toList());
    }

    @Transactional
    public void deleteChatroomSchedule(
        final Long chatroomId,
        final Long chatroomScheduleId
    )  {
        checkChatroomSchedule(chatroomId, chatroomScheduleId);

        chatroomScheduleCommand.deleteById(chatroomId);
    }

    private void checkAlreadyParticipant(final Long chatroomScheduleId, final UserId userId) {
        if (chatroomScheduleParticipantQuery.existsByChatroomScheduleUserUserId(chatroomScheduleId, userId)) {
            throw new AlreadyChatroomScheduleParticipantException();
        }
    }

    private void changeNullImageUrlToUserDefaultImageUrl(
        final List<ChatroomScheduleData> dataList,
        final String userImageDefaultUrl
    ) {
        dataList.forEach(data -> data.getParticipants().forEach(participantResponse -> {
            if (Objects.isNull(participantResponse.getImageUrl())) {
                participantResponse.updateImageUrl(userImageDefaultUrl);
            }
        }));
    }

    private ChatroomSchedule createChatroomSchedule(
        final User user,
        final Chatroom chatroom,
        final CreateChatroomScheduleRequest request
    ) {
        return chatroomScheduleCommand.save(request.toEntity(
            chatroom,
            user
        ));
    }

    private void checkChatroomSchedule(
        final Long chatroomId,
        final Long chatroomScheduleId
    ) {
        if (!chatroomScheduleQuery.existByChatroomIdAndChatroomScheduleId(chatroomId, chatroomScheduleId)) {
            throw new NotFoundChatroomScheduleException();
        }
    }

    private void createChatroomScheduleParticipant(
        final UserId userId,
        final Long chatroomScheduleId
    ) {
        chatroomScheduleParticipantCommand.save(
            ChatroomScheduleParticipant.builder()
                .chatroomSchedule(chatroomScheduleQuery.getReferenceById(chatroomScheduleId))
                .user(userQuery.getReferenceById(userId))
                .build()
        );
    }

    private void deleteChatroomScheduleParticipant(
        final UserId userId,
        final Long chatroomScheduleId
    ) {
        ChatroomScheduleParticipant chatroomScheduleParticipant = chatroomScheduleParticipantQuery.findByChatroomScheduleIdAndUserUserId(chatroomScheduleId, userId)
            .orElseThrow(NotAChatroomScheduleParticipantException::new);

        chatroomScheduleParticipantCommand.delete(chatroomScheduleParticipant);
    }
}
