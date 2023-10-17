package kr.co.pawpaw.api.service.board;

import kr.co.pawpaw.api.dto.board.BoardDto;
import kr.co.pawpaw.api.dto.board.BoardDto.BoardListDto;
import kr.co.pawpaw.api.dto.board.BoardDto.RegisterResponseDto;
import kr.co.pawpaw.api.dto.reply.ReplyDto.ReplyListDto;
import kr.co.pawpaw.api.service.boardImg.BoardImgService;
import kr.co.pawpaw.api.service.reply.ReplyService;
import kr.co.pawpaw.api.service.user.UserService;
import kr.co.pawpaw.common.exception.board.BoardException;
import kr.co.pawpaw.common.exception.board.BoardException.BoardNotFoundException;
import kr.co.pawpaw.common.exception.board.BoardException.BoardUpdateException;
import kr.co.pawpaw.common.exception.common.PermissionRequiredException;
import kr.co.pawpaw.common.exception.user.NotFoundUserException;
import kr.co.pawpaw.mysql.board.domain.Board;
import kr.co.pawpaw.mysql.board.service.command.BoardCommand;
import kr.co.pawpaw.mysql.board.service.query.BoardQuery;
import kr.co.pawpaw.mysql.user.domain.User;
import kr.co.pawpaw.mysql.user.domain.UserId;
import kr.co.pawpaw.mysql.user.service.query.UserQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class BoardService {

    private final UserQuery userQuery;
    private final BoardQuery boardQuery;
    private final BoardImgService imgService;
    private final BoardCommand boardCommand;
    private final ReplyService replyService;
    private final UserService userService;

    @Transactional
    public RegisterResponseDto register(UserId userId, BoardDto.BoardRegisterDto registerDto) {
        if (StringUtils.hasText(registerDto.getTitle()) && StringUtils.hasText(registerDto.getContent())) {

            User user = userQuery.findByUserId(userId).orElseThrow(NotFoundUserException::new);

            Board board = Board.builder()
                    .title(registerDto.getTitle())
                    .content(registerDto.getContent())
                    .writer(user.getNickname())
                    .user(user)
                    .build();
            boardCommand.save(board);

            return RegisterResponseDto.builder()
                    .title(board.getTitle())
                    .content(board.getContent())
                    .writer(user.getNickname())
                    .createDate(LocalDateTime.now())
                    .modifiedDate(LocalDateTime.now())
                    .build();
        } else {
            throw new BoardException.BoardRegisterException();
        }
    }

    @Transactional
    public BoardDto.BoardUpdateDto update(UserId userId, Long id, BoardDto.BoardUpdateDto updateDto) {

        User user = userQuery.findByUserId(userId).orElseThrow(NotFoundUserException::new);
        Board board = boardQuery.findById(id).orElseThrow(BoardNotFoundException::new);
        if (user.getUserId() != board.getUser().getUserId()) {
            throw new PermissionRequiredException();
        }

        if (updateDto.getTitle() != null || updateDto.getContent() != null) {
            board.updateTitleAndContent(updateDto.getTitle(), updateDto.getContent());
        } else {
            throw new BoardUpdateException();
        }

        return BoardDto.BoardUpdateDto.builder()
                .title(board.getTitle())
                .content(board.getContent())
                .build();
    }

    @Transactional
    public boolean removeBoard(UserId userId, Long id) {

        User user = userQuery.findByUserId(userId).orElseThrow(NotFoundUserException::new);
        Board board = boardQuery.findById(id).orElseThrow(BoardNotFoundException::new);
        if (user.getUserId() != board.getUser().getUserId()) {
            throw new PermissionRequiredException();
        }
        board.remove();
        return true;
    }

    @Transactional(readOnly = true)
    public Slice<BoardListDto> getBoardListWithRepliesBy(UserId userId, Pageable pageable) {
        userQuery.findByUserId(userId).orElseThrow(NotFoundUserException::new);

        Slice<Board> boardListWithReplies = boardQuery.getBoardListWithRepliesBy(pageable);

        List<BoardListDto> boardListDtos = getBoardListDtos(userId, pageable, boardListWithReplies);
        return new SliceImpl<>(boardListDtos, pageable, boardListWithReplies.hasNext());
    }

    @Transactional(readOnly = true)
    public Slice<BoardListDto> getBoardListWithRepliesByUser_UserId(UserId userId, Pageable pageable) {
        userQuery.findByUserId(userId).orElseThrow(NotFoundUserException::new);

        Slice<Board> boardListWithReplies = boardQuery.getBoardListWithRepliesByUser_UserId(pageable, userId);

        List<BoardListDto> boardListDtos = getBoardListDtos(userId, pageable, boardListWithReplies);
        return new SliceImpl<>(boardListDtos, pageable, boardListWithReplies.hasNext());
    }

    @Transactional(readOnly = true)
    public Slice<BoardListDto> searchBoardsByQuery(UserId userId, Pageable pageable, String query){
        userQuery.findByUserId(userId).orElseThrow(NotFoundUserException::new);
        if (!StringUtils.hasText(query) || query.isBlank())
            throw new BoardException.BoardSearchQueryException();

        Slice<Board> boardListWithRepliesForSearch = boardQuery.searchBoardsByQuery(query, pageable);

        List<BoardListDto> boardListDtos = getBoardListDtos(userId, pageable, boardListWithRepliesForSearch);

        return new SliceImpl<>(boardListDtos, pageable, boardListWithRepliesForSearch.hasNext());
    }

    private List<BoardListDto> getBoardListDtos(UserId userId, Pageable pageable, Slice<Board> boards) {
        // 게시글 리스트를 DTO로 변환
        List<BoardListDto> boardListDtos = boards.stream()
                .map(this::convertBoardToDto)
                .collect(Collectors.toList());

        boardListDtos.forEach(boardDto -> {
            List<ReplyListDto> replyList = replyService.findReplyListByBoardId(userId, boardDto.getId(), pageable).getContent();
            boardDto.setReplyListToBoard(replyList);
        });
        return boardListDtos;
    }

    private BoardListDto convertBoardToDto(Board board) {
        List<String> fileLink = imgService.viewFileImg(board.getId());
        String imageUrl = userService.whoAmI(board.getUser().getUserId()).getImageUrl();

        return BoardListDto.builder()
                .id(board.getId())
                .title(board.getTitle())
                .content(board.getContent())
                .likedCount(board.getLikedCount())
                .replyCount(board.getReplyCount())
                .userImageUrl(imageUrl)
                .fileNames(fileLink)
                .writer(board.getWriter())
                .createdDate(board.getCreatedDate())
                .modifiedDate(board.getModifiedDate())
                .build();
    }
}
