package kr.co.pawpaw.api.service.reply;

import kr.co.pawpaw.api.dto.reply.ReplyDto;
import kr.co.pawpaw.api.dto.reply.ReplyDto.ReplyListDto;
import kr.co.pawpaw.api.dto.reply.ReplyDto.ReplyRegisterDto;
import kr.co.pawpaw.api.dto.reply.ReplyDto.ReplyResponseDto;
import kr.co.pawpaw.api.service.user.UserService;
import kr.co.pawpaw.common.exception.board.BoardException.BoardNotFoundException;
import kr.co.pawpaw.common.exception.reply.ReplyException;
import kr.co.pawpaw.common.exception.reply.ReplyException.ReplyNotFoundException;
import kr.co.pawpaw.common.exception.user.NotFoundUserException;
import kr.co.pawpaw.mysql.board.domain.Board;
import kr.co.pawpaw.mysql.board.service.command.BoardCommand;
import kr.co.pawpaw.mysql.board.service.query.BoardQuery;
import kr.co.pawpaw.mysql.reply.domain.Reply;
import kr.co.pawpaw.mysql.reply.service.command.ReplyCommand;
import kr.co.pawpaw.mysql.reply.service.query.ReplyQuery;
import kr.co.pawpaw.mysql.user.domain.User;
import kr.co.pawpaw.mysql.user.domain.UserId;
import kr.co.pawpaw.mysql.user.service.query.UserQuery;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final UserService userService;

    @Transactional
    public ReplyResponseDto register(ReplyRegisterDto registerDto, UserId userId) {
        User user = userQuery.findByUserId(userId).orElseThrow(NotFoundUserException::new);
        Board board = boardQuery.findById(registerDto.getBoardId()).orElseThrow(BoardNotFoundException::new);
        Reply parentReply = replyQuery.findReplyById(registerDto.getParentId()).orElse(null);

        if (registerDto.getContent() == null)
            throw new ReplyException.ReplyRegisterException();

        if (parentReply == null)
            return registerReplyWhenNotExistParentReply(registerDto, user, board);
        else
            return registerReplyWhenExistParentReply(registerDto, user, board, parentReply);
    }

    private ReplyResponseDto registerReplyWhenExistParentReply(ReplyRegisterDto registerDto, User user, Board board, Reply parentReply) {

        Reply reply = Reply.builder()
                .user(user)
                .content(registerDto.getContent())
                .writer(user.getNickname())
                .board(board)
                .parent(parentReply)
                .build();

        Reply savedReply = replyCommand.save(reply);
        parentReply.getChild().add(savedReply); // 부모 댓글에 자식 댓글 추가
        board.plusReplyCount();

        return ReplyResponseDto.builder()
                .writer(user.getNickname())
                .content(savedReply.getContent())
                .boardId(registerDto.getBoardId())
                .parentId(registerDto.getParentId())
                .replyId(savedReply.getId())
                .build();
    }
    private ReplyResponseDto registerReplyWhenNotExistParentReply(ReplyRegisterDto registerDto, User user, Board board) {
        Reply reply = Reply.builder()
                .user(user)
                .content(registerDto.getContent())
                .writer(user.getNickname())
                .board(board)
                .build();

        Reply savedReply = replyCommand.save(reply);
        board.plusReplyCount();

        return ReplyResponseDto.builder()
                .writer(user.getNickname())
                .content(savedReply.getContent())
                .boardId(registerDto.getBoardId())
                .parentId(null)
                .replyId(savedReply.getId())
                .build();
    }

    @Transactional
    public ReplyResponseDto update(ReplyDto.ReplyUpdateDto updateDto, UserId userId, Long replyId) {
        Reply reply = replyQuery.findReplyByIdWithParentAndUser_UserId(replyId, userId).orElseThrow(ReplyNotFoundException::new);

        if (updateDto.getContent() == null)
            throw new ReplyException.ReplyUpdaterException();

        if (reply.getParent() != null) {
            Reply child = findChildById(reply.getParent(), reply.getId());
            child.updateContent(updateDto.getContent());
        } else
            reply.updateContent(updateDto.getContent());

        return ReplyResponseDto.builder()
                .writer(reply.getWriter())
                .content(updateDto.getContent())
                .boardId(reply.getBoard().getId())
                .replyId(reply.getId())
                .build();
    }

    private Reply findChildById(Reply parent, Long childId) {
        if (parent.getId().equals(childId)) {
            return parent;
        }
        for (Reply child : parent.getChild()) {
            Reply foundChild = findChildById(child, childId);
            if (foundChild != null) {
                return foundChild;
            }
        }
        throw new ReplyNotFoundException();
    }

    @Transactional
    public void remove(UserId userId, Long replyId, Long boardId) {
        Board board = boardQuery.findById(boardId).orElseThrow(BoardNotFoundException::new);
        Reply parentReply = replyQuery.findReplyByIdWithParentAndUser_UserId(replyId, userId).orElseThrow(ReplyNotFoundException::new);

        List<Reply> replies = replyQuery.findRepliesWithChildren(parentReply);
        replies.stream()
                .map(Reply::getId)
                .forEach(id -> replyQuery.removeReplyById(id));
        board.minusReplyCount();
    }

    @Transactional(readOnly = true)
    public Slice<ReplyListDto> findReplyListByBoardId(UserId userId, Long boardId, Pageable pageable) {
        userQuery.findByUserId(userId).orElseThrow(NotFoundUserException::new);
        boardQuery.findById(boardId).orElseThrow(BoardNotFoundException::new);

        Slice<Reply> replySlice = replyQuery.findReplyListByBoardId(boardId, pageable);
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
        boolean checkReplyWriter = replyQuery.checkReplyWriter(reply.getUser(), reply.getBoard());
        String userImageUrl = userService.whoAmI(reply.getUser().getUserId()).getImageUrl();
        if (reply.isRemoved()) {
            return new ReplyListDto(reply.getId(), "삭제된 댓글입니다.", null, true, userImageUrl);
        } else {
            ReplyListDto dto = new ReplyListDto(reply.getId(), reply.getContent(), reply.getWriter(), checkReplyWriter, userImageUrl);
            List<ReplyListDto> childDtos = reply.getChild().stream()
                    .map(this::convertCommentToDto)
                    .collect(Collectors.toList());
            dto.setChildToParentReply(childDtos);
            return dto;
        }
    }
}