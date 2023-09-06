package kr.co.pawpaw.api.application.reply;

import kr.co.pawpaw.api.dto.reply.ReplyDto;
import kr.co.pawpaw.api.dto.reply.ReplyDto.ReplyListDto;
import kr.co.pawpaw.api.dto.reply.ReplyDto.ReplyRegisterDto;
import kr.co.pawpaw.api.dto.reply.ReplyDto.ReplyResponseDto;
import kr.co.pawpaw.common.exception.board.BoardException.BoardNotFoundException;
import kr.co.pawpaw.common.exception.common.PermissionRequiredException;
import kr.co.pawpaw.common.exception.reply.ReplyException;
import kr.co.pawpaw.common.exception.reply.ReplyException.ReplyNotFoundException;
import kr.co.pawpaw.common.exception.user.NotFoundUserException;
import kr.co.pawpaw.domainrdb.board.domain.Board;
import kr.co.pawpaw.domainrdb.board.service.command.BoardCommand;
import kr.co.pawpaw.domainrdb.board.service.query.BoardQuery;
import kr.co.pawpaw.domainrdb.reply.domain.Reply;
import kr.co.pawpaw.domainrdb.reply.repository.ReplyRepository;
import kr.co.pawpaw.domainrdb.reply.service.command.ReplyCommand;
import kr.co.pawpaw.domainrdb.reply.service.query.ReplyQuery;
import kr.co.pawpaw.domainrdb.user.domain.User;
import kr.co.pawpaw.domainrdb.user.domain.UserId;
import kr.co.pawpaw.domainrdb.user.service.query.UserQuery;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Getter
@Slf4j
public class ReplyService {

    private final UserQuery userQuery;
    private final BoardQuery boardQuery;
    private final BoardCommand boardCommand;
    private final ReplyCommand replyCommand;
    private final ReplyQuery replyQuery;
    private final ReplyRepository replyRepository;

    @Transactional
    public ReplyResponseDto register(@RequestBody ReplyRegisterDto registerDto, UserId userId) {
        User user = userQuery.findByUserId(userId).orElseThrow(NotFoundUserException::new);
        Board board = boardQuery.findById(registerDto.getBoardId()).orElseThrow(BoardNotFoundException::new);
        Reply parentReply = null;
        if (registerDto.getParentId() != null) {
            parentReply = replyQuery.findById(registerDto.getParentId()).orElseThrow(ReplyNotFoundException::new);
        }
        Reply reply = Reply.builder()
                .user(user)
                .content(registerDto.getContent())
                .writer(user.getNickname())
                .board(board)
                .parent(parentReply)
                .build();
        if (reply.getContent() == null) {
            throw new ReplyException.ReplyRegisterException();
        }
        if (user.getUserId() != reply.getUser().getUserId()) {
            throw new PermissionRequiredException();
        }
        Reply savedReply = replyCommand.save(reply);
        board.plusReplyCount();
        boardCommand.save(board);

        if (parentReply != null) {
            parentReply.getChild().add(savedReply); // 부모 댓글에 자식 댓글 추가
            replyCommand.save(parentReply);
        }
        boardCommand.save(board);

        return ReplyResponseDto.builder()
                .writer(user.getNickname())
                .content(savedReply.getContent())
                .boardId(registerDto.getBoardId())
                .parentId(registerDto.getParentId())
                .replyId(savedReply.getId())
                .build();
    }

    @Transactional
    public ReplyResponseDto update(@RequestBody ReplyDto.ReplyUpdateDto updateDto, UserId userId, Long replyId) {
        User user = userQuery.findByUserId(userId).orElseThrow(NotFoundUserException::new);
        Reply reply = replyQuery.findById(replyId).orElseThrow(ReplyNotFoundException::new);

        if (updateDto.getContent() == null) {
            throw new ReplyException.ReplyUpdaterException();
        }
        if (user.getUserId() != reply.getUser().getUserId()) {
            throw new PermissionRequiredException();
        }
        if (reply.getParent() != null) {
            reply.getParent().getChild().forEach(child -> {
                if (child.getId().equals(reply.getId())) {
                    child.updateContent(updateDto.getContent());
                }
            });
        } else {
            reply.updateContent(updateDto.getContent());
        }
        replyCommand.save(reply);
        return ReplyResponseDto.builder()
                .writer(user.getNickname())
                .content(updateDto.getContent())
                .replyId(reply.getId())
                .build();
    }

    @Transactional
    public boolean remove(UserId userId, Long replyId, Long boardId) {
        User user = userQuery.findByUserId(userId).orElseThrow(NotFoundUserException::new);
        Board board = boardQuery.findById(boardId).orElseThrow(BoardNotFoundException::new);
        Reply parentReply = replyQuery.findReplyByIdWithParent(replyId).orElseThrow(ReplyNotFoundException::new);
        if (user.getUserId() != parentReply.getUser().getUserId()) {
            throw new PermissionRequiredException();
        }
        parentReply.remove();
        parentReply.getChild().forEach(child -> {
//            child.removeParent();
            child.remove();
            replyCommand.save(child);
        });
        board.minusReplyCount();
        return true;
    }

    @Transactional(readOnly = true)
    public Slice<ReplyListDto> findReplyListByBoardId(UserId userId, Long boardId, Pageable pageable) {
        userQuery.findByUserId(userId).orElseThrow(NotFoundUserException::new);
        boardQuery.findById(boardId).orElseThrow(BoardNotFoundException::new);

        Slice<Reply> replySlice = replyRepository.findReplyListByBoardId(boardId, pageable);
        List<ReplyListDto> replyList = convertNestedStructure(replySlice.getContent());

        return new SliceImpl<>(replyList, pageable, replySlice.hasNext());
    }

    private List<ReplyListDto> convertNestedStructure(List<Reply> replyList) {
        List<ReplyListDto> result = new ArrayList<>();
        Map<Long, ReplyListDto> map = new HashMap<>();

        replyList.stream().forEach(c -> {
            ReplyListDto dto = convertCommentToDto(c);
            map.put(dto.getId(), dto);
            if (c.getParent() != null) {
                map.get(c.getParent().getId()).getChildren().add(dto);
            } else result.add(dto);
        });
        return result;
    }

    public ReplyListDto convertCommentToDto(Reply reply) {
        if (reply.isRemoved()) {
            return new ReplyListDto(reply.getId(), "삭제된 댓글입니다.", null);
        } else {
            ReplyListDto dto = new ReplyListDto(reply.getId(), reply.getContent(), reply.getWriter());
            List<ReplyListDto> childDtos = reply.getChild().stream()
                    .map(this::convertCommentToDto)
                    .collect(Collectors.toList());
            dto.setChildren(childDtos);
            return dto;
        }
    }
}