package kr.co.pawpaw.api.service.chatroom;

import kr.co.pawpaw.api.dto.chatroom.*;
import kr.co.pawpaw.api.service.file.FileService;
import kr.co.pawpaw.api.util.file.FileUtil;
import kr.co.pawpaw.common.exception.chatroom.*;
import kr.co.pawpaw.common.exception.user.NotFoundUserException;
import kr.co.pawpaw.dynamodb.chat.domain.Chat;
import kr.co.pawpaw.dynamodb.chat.domain.ChatType;
import kr.co.pawpaw.dynamodb.chat.dto.ChatMessageDto;
import kr.co.pawpaw.dynamodb.chat.service.command.ChatCommand;
import kr.co.pawpaw.dynamodb.chat.service.query.ChatQuery;
import kr.co.pawpaw.dynamodb.util.chat.ChatUtil;
import kr.co.pawpaw.mysql.chatroom.domain.*;
import kr.co.pawpaw.mysql.chatroom.dto.*;
import kr.co.pawpaw.mysql.chatroom.service.command.ChatroomCommand;
import kr.co.pawpaw.mysql.chatroom.service.command.ChatroomParticipantCommand;
import kr.co.pawpaw.mysql.chatroom.service.command.TrendingChatroomCommand;
import kr.co.pawpaw.mysql.chatroom.service.command.ChatroomHashTagCommand;
import kr.co.pawpaw.mysql.chatroom.service.query.ChatroomDefaultCoverQuery;
import kr.co.pawpaw.mysql.chatroom.service.query.ChatroomParticipantQuery;
import kr.co.pawpaw.mysql.chatroom.service.query.ChatroomQuery;
import kr.co.pawpaw.mysql.chatroom.service.query.TrendingChatroomQuery;
import kr.co.pawpaw.mysql.storage.domain.File;
import kr.co.pawpaw.mysql.user.domain.User;
import kr.co.pawpaw.mysql.user.domain.UserId;
import kr.co.pawpaw.mysql.user.dto.ChatMessageUserDto;
import kr.co.pawpaw.mysql.user.service.query.UserQuery;
import kr.co.pawpaw.redis.service.pub.RedisPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatroomService {
    private final ChatQuery chatQuery;
    private final ChatCommand chatCommand;
    private final ChannelTopic chatTopic;
    private final ChatroomCommand chatroomCommand;
    private final ChatroomParticipantCommand chatroomParticipantCommand;
    private final ChatroomParticipantQuery chatroomParticipantQuery;
    private final ChatroomDefaultCoverQuery chatroomDefaultCoverQuery;
    private final TrendingChatroomCommand trendingChatroomCommand;
    private final TrendingChatroomQuery trendingChatroomQuery;
    private final ChatroomQuery chatroomQuery;
    private final UserQuery userQuery;
    private final FileService fileService;
    private final RedisPublisher redisPublisher;
    private final ChatroomHashTagCommand chatroomHashTagCommand;

    @Transactional(readOnly = true)
    public List<ChatroomResponse> searchChatroom(final String query, final UserId userId) {
        return chatroomQuery.findBySearchQuery(query, userId);
    }

    @Transactional
    public void deleteChatroom(final Long chatroomId) {
        List<ChatroomParticipant> chatroomParticipantList = chatroomParticipantQuery.findAllByChatroomId(chatroomId);

        if (chatroomParticipantList.size() > 1) {
            throw new ChatroomParticipantExistException();
        }

        chatroomParticipantCommand.delete(chatroomParticipantList.get(0));
        trendingChatroomCommand.deleteByChatroomId(chatroomId);
        chatroomCommand.deleteById(chatroomId);
    }

    @Transactional
    public void updateChatroomManager(
        final UserId userId,
        final Long chatroomId,
        final UpdateChatroomManagerRequest request
    ) {
        ChatroomParticipant currentManager = chatroomParticipantQuery.findByUserIdAndChatroomId(userId, chatroomId)
            .orElseThrow(NotAChatroomParticipantException::new);

        ChatroomParticipant nextManager = chatroomParticipantQuery.findByUserIdAndChatroomId(request.getNextManagerId(), chatroomId)
            .orElseThrow(NotAChatroomParticipantException::new);

        if (nextManager.isManager()) {
            throw new AlreadyChatroomManagerException();
        }

        currentManager.updateRole(ChatroomParticipantRole.PARTICIPANT);
        nextManager.updateRole(ChatroomParticipantRole.MANAGER);

        Chatroom chatroom = chatroomQuery.findById(chatroomId)
            .orElseThrow(NotFoundChatroomException::new);

        chatroom.updateManager(nextManager);

        saveAndPublishChatMessage(ChatType.CHANGE_MANAGER, ChatUtil.getChangeManagerDataFromNickname(nextManager.getUser().getNickname()), chatroomId);
    }

    @Transactional(readOnly = true)
    public ChatroomSimpleResponse getChatroomInfo(final Long chatroomId) {
        return chatroomQuery.findByChatroomIdAsSimpleResponse(chatroomId);
    }

    @Transactional
    public void sendChatImage(
        final UserId userId,
        final Long chatroomId,
        final MultipartFile multipartFile
    ) {
        User user = userQuery.findByUserId(userId)
            .orElseThrow(NotFoundUserException::new);

        Chat imageChat = createImageChat(userId, chatroomId, multipartFile);

        redisPublisher.publish(
            chatTopic,
            ChatMessageDto.of(
                imageChat,
                user.getNickname(),
                user.getUserImage().getFileUrl()
            ));
    }

    private Chat createImageChat(UserId userId, Long chatroomId, MultipartFile multipartFile) {
        File imageChatFile = fileService.saveFileByMultipartFile(multipartFile, userId);

        return chatCommand.save(Chat.builder()
                .senderId(userId.getValue())
                .chatroomId(chatroomId)
                .chatType(ChatType.IMAGE)
                .data(imageChatFile.getFileUrl())
            .build());
    }

    @Transactional
    public void inviteUser(
        final Long chatroomId,
        final InviteChatroomUserRequest request
    ) {
        User user = userQuery.findByUserId(request.getUserId())
            .orElseThrow(NotFoundUserException::new);

        checkAlreadyChatroomParticipant(chatroomId, request.getUserId());
        createChatroomParticipantByChatroomIdAndInviteChatroomUserRequest(chatroomId, request);
        saveAndPublishChatMessage(ChatType.INVITE, ChatUtil.getInviteDataFromNickname(user.getNickname()), chatroomId);
    }

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
        createChatroomHashTags(request, chatroom);

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
        createChatroomHashTags(request, chatroom);

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

        checkAlreadyChatroomParticipant(chatroomId, user.getUserId());

        joinChatroomAsParticipant(chatroomId, user);

        createTrendingChatroom(chatroomId);

        saveAndPublishChatMessage(ChatType.JOIN, ChatUtil.getJoinDataFromNickname(user.getNickname()), chatroomId);
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

        saveAndPublishChatMessage(ChatType.LEAVE, ChatUtil.getLeaveDataFromNickname(user.getNickname()), chatroomId);
    }

    @Transactional(readOnly = true)
    public List<ChatroomParticipantResponse> getChatroomParticipantResponseList(final Long chatroomId) {
        return chatroomParticipantQuery.getChatroomParticipantResponseList(chatroomId);
    }

    @Transactional(readOnly = true)
    public List<ChatroomNonParticipantResponse> searchChatroomNonParticipants(
        final Long chatroomId,
        final String nicknameKeyword
    ) {
        return userQuery.searchChatroomNonParticipant(chatroomId, nicknameKeyword);
    }

    public Slice<ChatMessageDto> findBeforeChatMessages(
        final Long chatroomId,
        final Long targetId,
        final int size
    ) {
        Slice<Chat> chatSlice = chatQuery.findWithSliceByChatroomIdAndSortIdLessThan(
            chatroomId,
            targetId,
            PageRequest.of(0, size, Sort.by(Sort.Direction.DESC, "sortId"))
        );

        Map<String, ChatMessageUserDto> chatMessageUserDtoMap = userQuery.getChatMessageUserDtoByUserIdIn(chatSlice.get()
                .map(Chat::getSenderId)
                .filter(Objects::nonNull)
                .map(UserId::of)
                .collect(Collectors.toList())
            )
            .stream()
            .collect(Collectors.toMap(dto -> dto.getUserId().getValue(), Function.identity()));

        return chatSlice.map(chat -> {
            ChatMessageUserDto dto = chatMessageUserDtoMap.get(chat.getSenderId());

            if (Objects.nonNull(dto)) {
                return ChatMessageDto.of(chat, dto.getNickname(), dto.getImageUrl());
            } else {
                return ChatMessageDto.of(chat, null, null);
            }
        });
    }

    public List<ChatroomDetailResponse> getParticipatedChatroomList(final UserId userId) {
        List<ChatroomDetailData> beforeProcessDataList = chatroomQuery.getParticipatedChatroomDetailDataByUserId(userId);

        Map<Long, LocalDateTime> chatroomLastChatTimeMap = beforeProcessDataList.stream()
            .map(ChatroomDetailData::getId)
            .distinct()
            .map(chatQuery::findFirstByChatroomIdOrderBySortIdDesc)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .collect(Collectors.toMap(Chat::getChatroomId, Chat::getCreatedDate));

        return beforeProcessDataList
            .stream()
            .map(chatroomDetailData -> ChatroomDetailResponse.of(chatroomDetailData, chatroomLastChatTimeMap.get(chatroomDetailData.getId())))
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

    private void saveAndPublishChatMessage(
        final ChatType chatType,
        final String data,
        final Long chatroomId
    ) {
        Chat chat = chatCommand.save(Chat.builder()
                .chatroomId(chatroomId)
                .chatType(chatType)
                .data(data)
            .build());

        redisPublisher.publish(chatTopic, ChatMessageDto.of(
            chat,
            null,
            null
        ));
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
        final UserId userId
    ) {
        if (chatroomParticipantQuery.existsByUserIdAndChatroomId(userId, chatroomId)) {
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

    private void createChatroomHashTags(
        final CreateChatroomRequest request,
        final Chatroom chatroom
    ) {
        chatroomHashTagCommand.saveAll(request.toChatroomHashTagList(chatroom));
    }

    private void createChatroomHashTags(
        final CreateChatroomWithDefaultCoverRequest request,
        final Chatroom chatroom
    ) {
        chatroomHashTagCommand.saveAll(request.toChatroomHashTagList(chatroom));
    }

    private Chatroom createChatroomByRequestAndCoverFile(
        final CreateChatroomWithDefaultCoverRequest request,
        final File coverFile
    ) {
        return chatroomCommand.save(request.toChatroom(coverFile));
    }

    private void createChatroomParticipantByChatroomIdAndInviteChatroomUserRequest(
        final Long chatroomId,
        final InviteChatroomUserRequest request
    ) {
        chatroomParticipantCommand.save(
            ChatroomParticipant.builder()
                .chatroom(chatroomQuery.getReferenceById(chatroomId))
                .user(userQuery.getReferenceById(request.getUserId()))
                .role(ChatroomParticipantRole.PARTICIPANT)
                .build());
    }
}
