package kr.co.pawpaw.api.service.chatroom;

import kr.co.pawpaw.api.dto.chatroom.ChatroomScheduleResponse;
import kr.co.pawpaw.api.dto.chatroom.CreateChatroomScheduleRequest;
import kr.co.pawpaw.api.dto.chatroom.CreateChatroomScheduleResponse;
import kr.co.pawpaw.common.exception.chatroom.NotAChatroomParticipantException;
import kr.co.pawpaw.common.exception.chatroom.NotAChatroomScheduleParticipantException;
import kr.co.pawpaw.common.exception.chatroom.NotFoundChatroomScheduleException;
import kr.co.pawpaw.domainrdb.chatroom.domain.Chatroom;
import kr.co.pawpaw.domainrdb.chatroom.domain.ChatroomSchedule;
import kr.co.pawpaw.domainrdb.chatroom.domain.ChatroomScheduleParticipant;
import kr.co.pawpaw.domainrdb.chatroom.service.command.ChatroomScheduleCommand;
import kr.co.pawpaw.domainrdb.chatroom.service.command.ChatroomScheduleParticipantCommand;
import kr.co.pawpaw.domainrdb.chatroom.service.query.ChatroomParticipantQuery;
import kr.co.pawpaw.domainrdb.chatroom.service.query.ChatroomQuery;
import kr.co.pawpaw.domainrdb.chatroom.service.query.ChatroomScheduleParticipantQuery;
import kr.co.pawpaw.domainrdb.chatroom.service.query.ChatroomScheduleQuery;
import kr.co.pawpaw.domainrdb.user.domain.User;
import kr.co.pawpaw.domainrdb.user.domain.UserId;
import kr.co.pawpaw.domainrdb.user.service.query.UserQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatroomScheduleService {
    private final ChatroomScheduleQuery chatroomScheduleQuery;
    private final ChatroomScheduleCommand chatroomScheduleCommand;
    private final ChatroomScheduleParticipantCommand chatroomScheduleParticipantCommand;
    private final ChatroomScheduleParticipantQuery chatroomScheduleParticipantQuery;
    private final ChatroomParticipantQuery chatroomParticipantQuery;
    private final UserQuery userQuery;
    private final ChatroomQuery chatroomQuery;

    public CreateChatroomScheduleResponse createChatroomSchedule(
        final UserId userId,
        final Long chatroomId,
        final CreateChatroomScheduleRequest request
    ) {
        checkChatroomParticipant(userId, chatroomId);

        ChatroomSchedule chatroomSchedule = createChatroomSchedule(
            userQuery.getReferenceById(userId),
            chatroomQuery.getReferenceById(chatroomId),
            request
        );

        return CreateChatroomScheduleResponse.of(chatroomSchedule);
    }

    public void participateChatroomSchedule(
        final UserId userId,
        final Long chatroomId,
        final Long chatroomScheduleId
    ) {
        checkChatroomParticipant(userId, chatroomId);
        checkChatroomSchedule(chatroomId, chatroomScheduleId);
        createChatroomScheduleParticipant(userId, chatroomScheduleId);
    }

    public void leaveChatroomSchedule(
        final UserId userId,
        final Long chatroomId,
        final Long chatroomScheduleId
    ) {
        checkChatroomParticipant(userId, chatroomId);
        checkChatroomSchedule(chatroomId, chatroomScheduleId);
        deleteChatroomScheduleParticipant(userId, chatroomScheduleId);
    }

    public List<ChatroomScheduleResponse> getNotEndChatroomScheduleList(
        final UserId userId,
        final Long chatroomId
    ) {
        checkChatroomParticipant(userId, chatroomId);
        return chatroomScheduleQuery.findNotEndChatroomScheduleByChatroomId(chatroomId)
            .stream()
            .map(ChatroomScheduleResponse::of)
            .collect(Collectors.toList());
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

    private void checkChatroomParticipant(
        final UserId userId,
        final Long chatroomId
    ) {
        if (!chatroomParticipantQuery.existsByUserIdAndChatroomId(userId, chatroomId)) {
            throw new NotAChatroomParticipantException();
        }
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
