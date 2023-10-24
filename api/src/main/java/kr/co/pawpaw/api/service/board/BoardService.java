package kr.co.pawpaw.api.service.board;

import kr.co.pawpaw.api.dto.board.BoardDto;
import kr.co.pawpaw.api.dto.board.BoardDto.BoardListDto;
import kr.co.pawpaw.api.dto.board.BoardDto.BoardResponseDto;
import kr.co.pawpaw.api.dto.reply.ReplyDto.ReplyListDto;
import kr.co.pawpaw.api.service.boardlike.BoardLikeService;
import kr.co.pawpaw.api.service.bookmark.BookmarkService;
import kr.co.pawpaw.api.service.file.FileService;
import kr.co.pawpaw.api.service.reply.ReplyService;
import kr.co.pawpaw.common.exception.board.BoardException;
import kr.co.pawpaw.common.exception.board.BoardException.BoardNotFoundException;
import kr.co.pawpaw.common.exception.common.PermissionRequiredException;
import kr.co.pawpaw.common.exception.user.NotFoundUserException;
import kr.co.pawpaw.mysql.board.domain.Board;
import kr.co.pawpaw.mysql.board.service.command.BoardCommand;
import kr.co.pawpaw.mysql.board.service.query.BoardQuery;
import kr.co.pawpaw.mysql.storage.domain.File;
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
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class BoardService {

    private final UserQuery userQuery;
    private final BoardQuery boardQuery;
    private final BoardCommand boardCommand;
    private final ReplyService replyService;
    private final BoardLikeService boardLikeService;
    private final BookmarkService bookmarkService;
    private final FileService fileService;

    @Transactional
    public BoardResponseDto register(UserId userId, BoardDto.BoardRegisterDto registerDto, List<MultipartFile> files) {
        if (StringUtils.hasText(registerDto.getContent())) {

            User user = userQuery.findByUserId(userId).orElseThrow(NotFoundUserException::new);

            List<String> fileUrls = new ArrayList<>();
            if (files != null) {
                fileUrls = files.stream()
                        .map(file -> fileService.saveFileByMultipartFile(file, userId).getFileUrl())
                        .collect(Collectors.toList());
            }

            Board board = Board.builder()
                    .content(registerDto.getContent())
                    .writer(user.getNickname())
                    .user(user)
                    .fileUrls(fileUrls)
                    .build();

            boardCommand.save(board);

            return BoardResponseDto.builder()
                    .id(board.getId())
                    .content(board.getContent())
                    .writer(user.getNickname())
                    .createDate(LocalDateTime.now())
                    .modifiedDate(LocalDateTime.now())
                    .fileUrls(fileUrls)
                    .build();
        } else {
            throw new BoardException.BoardRegisterException();
        }
    }

    @Transactional
    public BoardResponseDto update(UserId userId, Long id, BoardDto.BoardUpdateDto updateDto, List<MultipartFile> files) {

        User user = userQuery.findByUserId(userId).orElseThrow(NotFoundUserException::new);
        Board board = boardQuery.findById(id).orElseThrow(BoardNotFoundException::new);

        if (user.getUserId() != board.getUser().getUserId()) {
            throw new PermissionRequiredException();
        }

        if (StringUtils.hasText(updateDto.getContent())) {
            board.updateContent(updateDto.getContent());
        }

        // 이미지 파일 업데이트 (만약 파일이 업로드되었다면)
        if (files != null && !files.isEmpty()) {
            List<String> fileUrls = files.stream()
                    .map(file -> fileService.saveFileByMultipartFile(file, userId).getFileUrl())
                    .collect(Collectors.toList());

            board.updateFileUrl(fileUrls);
        }

        boardCommand.save(board);

        return BoardResponseDto.builder()
                .id(board.getId())
                .content(board.getContent())
                .writer(user.getNickname())
                .createDate(board.getCreatedDate())
                .modifiedDate(LocalDateTime.now())
                .fileUrls(board.getFileUrls())
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
    public BoardListDto getBoardWithRepliesBy(long boardId, UserId userId, Pageable pageable) {
        userQuery.findByUserId(userId).orElseThrow(NotFoundUserException::new);
        Board board = boardQuery.getBoardWithRepliesBy(boardId);
        BoardListDto boardListDto = convertBoardToDto(board, userId);

        Slice<ReplyListDto> replyListByBoardId = replyService.findReplyListByBoardId(userId, boardId, pageable);
        boardListDto.setReplyListToBoard(replyListByBoardId.getContent());
        return boardListDto;
    }

    @Transactional(readOnly = true)
    public Slice<BoardListDto> getBoardListWithRepliesByUser_UserId(UserId userId, Pageable pageable) {
        userQuery.findByUserId(userId).orElseThrow(NotFoundUserException::new);

        Slice<Board> boardListWithReplies = boardQuery.getBoardListWithRepliesByUser_UserId(pageable, userId);

        List<BoardListDto> boardListDtos = getBoardListDtos(userId, pageable, boardListWithReplies);
        return new SliceImpl<>(boardListDtos, pageable, boardListWithReplies.hasNext());
    }

    @Transactional(readOnly = true)
    public Slice<BoardListDto> searchBoardsByQuery(UserId userId, Pageable pageable, String query) {
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
                .map(board -> convertBoardToDto(board, userId))
                .collect(Collectors.toList());

        boardListDtos.forEach(boardDto -> {
            Slice<ReplyListDto> replyList = replyService.findReplyListByBoardId(userId, boardDto.getId(), pageable);
            boardDto.setReplyListToBoard(replyList.getContent());

            Board board = boardQuery.findBoardWithFileUrlsById(boardDto.getId());
            boardDto.setFileUrlsToBoard(board.getFileUrls());
        });
        return boardListDtos;
    }

    private BoardListDto convertBoardToDto(Board board, UserId userId) {
        String imageUrl = board.getUser().getUserImage().getFileUrl();
        boolean existBoardLike = boardLikeService.checkLikeExist(board.getUser(), board);
        boolean existBookmark = bookmarkService.existsByUser_UserIdAndBoard(userId, board);

        return BoardListDto.builder()
                .userId(board.getUser().getUserId())
                .id(board.getId())
                .content(board.getContent())
                .likedCount(board.getLikedCount())
                .replyCount(board.getReplyCount())
                .userImageUrl(imageUrl)
                .boardLiked(existBoardLike)
                .bookmarked(existBookmark)
                .writer(board.getWriter())
                .createdDate(board.getCreatedDate())
                .modifiedDate(board.getModifiedDate())
                .build();
    }
}
