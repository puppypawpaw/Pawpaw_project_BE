package kr.co.pawpaw.api.service.chatroom;

import kr.co.pawpaw.api.dto.chatroom.*;
import kr.co.pawpaw.api.service.file.FileService;
import kr.co.pawpaw.api.service.user.UserService;
import kr.co.pawpaw.api.util.file.FileUtil;
import kr.co.pawpaw.common.exception.chatroom.AlreadyChatroomParticipantException;
import kr.co.pawpaw.common.exception.chatroom.IsNotChatroomParticipantException;
import kr.co.pawpaw.common.exception.chatroom.NotAllowedChatroomLeaveException;
import kr.co.pawpaw.common.exception.chatroom.NotFoundChatroomDefaultCoverException;
import kr.co.pawpaw.common.exception.user.NotFoundUserException;
import kr.co.pawpaw.domainrdb.chatroom.domain.*;
import kr.co.pawpaw.domainrdb.chatroom.dto.ChatroomCoverResponse;
import kr.co.pawpaw.domainrdb.chatroom.dto.ChatroomParticipantResponse;
import kr.co.pawpaw.domainrdb.chatroom.dto.ChatroomResponse;
import kr.co.pawpaw.domainrdb.chatroom.dto.TrendingChatroomResponse;
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
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatroomService {
    private final ChatroomCommand chatroomCommand;
    private final ChatroomParticipantCommand chatroomParticipantCommand;
    private final ChatroomParticipantQuery chatroomParticipantQuery;
    private final ChatroomDefaultCoverQuery chatroomDefaultCoverQuery;
    private final TrendingChatroomCommand trendingChatroomCommand;
    private final TrendingChatroomQuery trendingChatroomQuery;
    private final ChatroomQuery chatroomQuery;
    private final UserQuery userQuery;
    private final FileService fileService;
    private final UserService userService;

    @Transactional
    public CreateChatroomResponse createChatroom(
        final UserId userId,
        final CreateChatroomRequest request,
        final MultipartFile chatroomCoverMultipartFile
    ) {
        User user = userQuery.findByUserId(userId)
            .orElseThrow(NotFoundUserException::new);

        File coverFile = createChatroomCoverIfExists(userId, chatroomCoverMultipartFile);
        Chatroom chatroom = createChatroomByRequestAndCoverFile(request, coverFile);

        joinChatroomAsManager(chatroom, user);

        return CreateChatroomResponse.of(chatroom);
    }

    @Transactional
    public CreateChatroomResponse createChatroomWithDefaultCover(
        final UserId userId,
        final CreateChatroomWithDefaultCoverRequest request
    ) {
        User user = userQuery.findByUserId(userId)
            .orElseThrow(NotFoundUserException::new);

        ChatroomDefaultCover chatroomDefaultCover = chatroomDefaultCoverQuery.findById(request.getCoverId())
            .orElseThrow(NotFoundChatroomDefaultCoverException::new);

        Chatroom chatroom = createChatroomByRequestAndCoverFile(request, chatroomDefaultCover.getCoverFile());

        joinChatroomAsManager(chatroom, user);

        return CreateChatroomResponse.of(chatroom);
    }

    @Transactional
    public void joinChatroom(
        final UserId userId,
        final Long chatroomId
    ) {
        User user = userQuery.findByUserId(userId)
            .orElseThrow(NotFoundUserException::new);

        checkAlreadyChatroomParticipant(chatroomId, user);

        joinChatroomAsParticipant(chatroomId, user);

        createTrendingChatroom(chatroomId);
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

    public List<ChatroomParticipantResponse> getChatroomParticipantResponseList(final Long chatroomId) {
        String defaultImageUrl = userService.getUserDefaultImageUrl();

        return chatroomParticipantQuery.getChatroomParticipantResponseList(chatroomId)
            .stream()
            .peek(updateToDefaultIfNull(defaultImageUrl))
            .collect(Collectors.toList());
    }

    private static Consumer<ChatroomParticipantResponse> updateToDefaultIfNull(String defaultImageUrl) {
        return response -> {
            if (Objects.isNull(response.getImageUrl())) response.updateImageUrl(defaultImageUrl);
        };
    }

    public List<ChatroomDetailResponse> getParticipatedChatroomList(final UserId userId) {
        return chatroomQuery.getParticipatedChatroomDetailDataByUserId(userId)
            .stream()
            .map(ChatroomDetailResponse::of)
            .collect(Collectors.toList());
    }

    public List<ChatroomResponse> getRecommendedNewChatroomList(final UserId userId) {
        return chatroomQuery.getAccessibleNewChatroomByUserId(userId);
    }

    public Slice<TrendingChatroomResponse> getTrendingChatroomList(
        final UserId userId,
        final Long beforeId,
        final int size
    ) {
        return chatroomQuery.getAccessibleTrendingChatroom(userId, beforeId, size);
    }

    public List<ChatroomCoverResponse> getChatroomDefaultCoverList() {
        return chatroomDefaultCoverQuery.findAllChatroomCover();
    }

    private void createTrendingChatroom(final Long chatroomId) {
        if (!trendingChatroomQuery.existsByChatroomId(chatroomId)) {
            trendingChatroomCommand.save(TrendingChatroom.builder()
                .chatroom(chatroomQuery.getReferenceById(chatroomId))
                .build());
        }
    }

    private void checkAlreadyChatroomParticipant(
        final Long chatroomId,
        final User user
    ) {
        if (chatroomParticipantQuery.existsByUserIdAndChatroomId(user.getUserId(), chatroomId)) {
            throw new AlreadyChatroomParticipantException();
        }
    }

    private File createChatroomCoverIfExists(
        final UserId userId,
        final MultipartFile chatroomCoverMultipartFile
    ) {
        if (chatroomCoverMultipartFile != null && FileUtil.getByteLength(chatroomCoverMultipartFile) > 0) {
            return fileService.saveFileByMultipartFile(chatroomCoverMultipartFile, userId);
        }

        return null;
    }

    private void joinChatroomAsManager(
        final Chatroom chatroom,
        final User user
    ) {
        ChatroomParticipant manager = ChatroomParticipant.builder()
            .chatroom(chatroom)
            .role(ChatroomParticipantRole.MANAGER)
            .user(user)
            .build();

        chatroomParticipantCommand.save(manager);

        chatroom.updateManager(manager);
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

    private Chatroom createChatroomByRequestAndCoverFile(
        final CreateChatroomRequest request,
        final File coverFile
    ) {
        return chatroomCommand.save(request.toChatroom(coverFile));
    }

    private Chatroom createChatroomByRequestAndCoverFile(
        final CreateChatroomWithDefaultCoverRequest request,
        final File coverFile
    ) {
        return chatroomCommand.save(request.toChatroom(coverFile));
    }
}
