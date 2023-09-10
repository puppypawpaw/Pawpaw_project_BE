package kr.co.pawpaw.api.application.chatroom;

import kr.co.pawpaw.api.application.file.FileService;
import kr.co.pawpaw.api.dto.chatroom.CreateChatroomRequest;
import kr.co.pawpaw.api.dto.chatroom.CreateChatroomResponse;
import kr.co.pawpaw.api.util.file.FileUtil;
import kr.co.pawpaw.common.exception.chatroom.IsNotChatroomParticipantException;
import kr.co.pawpaw.common.exception.chatroom.NotAllowedChatroomLeaveException;
import kr.co.pawpaw.common.exception.user.NotFoundUserException;
import kr.co.pawpaw.domainrdb.chatroom.domain.Chatroom;
import kr.co.pawpaw.domainrdb.chatroom.domain.ChatroomCover;
import kr.co.pawpaw.domainrdb.chatroom.domain.ChatroomParticipant;
import kr.co.pawpaw.domainrdb.chatroom.domain.ChatroomParticipantRole;
import kr.co.pawpaw.domainrdb.chatroom.service.command.ChatroomCommand;
import kr.co.pawpaw.domainrdb.chatroom.service.command.ChatroomCoverCommand;
import kr.co.pawpaw.domainrdb.chatroom.service.command.ChatroomHashTagCommand;
import kr.co.pawpaw.domainrdb.chatroom.service.command.ChatroomParticipantCommand;
import kr.co.pawpaw.domainrdb.chatroom.service.query.ChatroomParticipantQuery;
import kr.co.pawpaw.domainrdb.chatroom.service.query.ChatroomQuery;
import kr.co.pawpaw.domainrdb.storage.domain.File;
import kr.co.pawpaw.domainrdb.user.domain.User;
import kr.co.pawpaw.domainrdb.user.domain.UserId;
import kr.co.pawpaw.domainrdb.user.service.query.UserQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ChatroomService {
    private final ChatroomCommand chatroomCommand;
    private final ChatroomCoverCommand chatroomCoverCommand;
    private final ChatroomHashTagCommand chatroomHashTagCommand;
    private final ChatroomParticipantCommand chatroomParticipantCommand;
    private final ChatroomParticipantQuery chatroomParticipantQuery;
    private final ChatroomQuery chatroomQuery;
    private final UserQuery userQuery;
    private final FileService fileService;

    @Transactional
    public CreateChatroomResponse createChatroom(
        final UserId userId,
        final CreateChatroomRequest request,
        final MultipartFile chatroomCoverMultipartFile
    ) {
        User user = userQuery.findByUserId(userId)
            .orElseThrow(NotFoundUserException::new);

        Chatroom chatroom = createChatroomByRequest(request);

        createChatroomCoverIfExists(userId, chatroomCoverMultipartFile, chatroom);
        createChatroomHashTags(chatroom, request);
        joinChatroomAsManager(chatroom, user);

        return CreateChatroomResponse.of(chatroom);
    }

    @Transactional
    public void joinChatroom(
        final UserId userId,
        final Long chatRoomId
    ) {
        User user = userQuery.findByUserId(userId)
            .orElseThrow(NotFoundUserException::new);

        joinChatroomAsParticipant(chatRoomId, user);
    }

    @Transactional
    public void leaveChatroom(
        final UserId userId,
        final Long chatroomId
    ) {
        User user = userQuery.findByUserId(userId)
            .orElseThrow(NotFoundUserException::new);

        ChatroomParticipant chatroomParticipant = chatroomParticipantQuery.findByUserIdAndChatroomId(userId, chatroomId)
            .orElseThrow(IsNotChatroomParticipantException::new);

        if (chatroomParticipant.isManager()) {
            throw new NotAllowedChatroomLeaveException();
        }

        chatroomParticipantCommand.delete(chatroomParticipant);
    }

    private void createChatroomCoverIfExists(
        final UserId userId,
        final MultipartFile chatroomCoverMultipartFile,
        final Chatroom chatroom
    ) {
        if (chatroomCoverMultipartFile != null && FileUtil.getByteLength(chatroomCoverMultipartFile) > 0) {
            File chatroomCoverFile = fileService.saveFileByMultipartFile(chatroomCoverMultipartFile, userId);

            createChatroomCover(chatroom, chatroomCoverFile);
        }
    }

    private void joinChatroomAsManager(
        final Chatroom chatroom,
        final User user
    ) {
        chatroomParticipantCommand.save(
            ChatroomParticipant.builder()
                .chatroom(chatroom)
                .role(ChatroomParticipantRole.MANAGER)
                .user(user)
                .build());
    }

    private void joinChatroomAsParticipant(
        final Long chatRoomId,
        final User user
    ) {
        chatroomParticipantCommand.save(
            ChatroomParticipant.builder()
                .chatroom(chatroomQuery.getReferenceById(chatRoomId))
                .role(ChatroomParticipantRole.PARTICIPANT)
                .user(user)
            .build());
    }

    private void createChatroomCover(
        final Chatroom chatroom,
        final File chatroomCoverFile
    ) {
        chatroomCoverCommand.save(
            ChatroomCover.builder()
                .chatroom(chatroom)
                .coverFile(chatroomCoverFile)
                .build());
    }

    private Chatroom createChatroomByRequest(
        final CreateChatroomRequest request
    ) {
        return chatroomCommand.save(request.toChatroom());
    }

    private void createChatroomHashTags(
        final Chatroom chatroom,
        final CreateChatroomRequest request
    ) {
        chatroomHashTagCommand.saveAll(request.toChatroomHashTags(chatroom));
    }
}
