package kr.co.pawpaw.api.application.board;

import kr.co.pawpaw.api.dto.board.BoardDto;
import kr.co.pawpaw.api.dto.board.BoardDto.RegisterResponseDto;
import kr.co.pawpaw.common.exception.board.BoardException;
import kr.co.pawpaw.common.exception.board.BoardException.BoardDeleteException;
import kr.co.pawpaw.common.exception.board.BoardException.BoardNotFoundException;
import kr.co.pawpaw.common.exception.board.BoardException.BoardUpdateException;
import kr.co.pawpaw.common.exception.common.PermissionRequiredException;
import kr.co.pawpaw.common.exception.user.NotFoundUserException;
import kr.co.pawpaw.domainrdb.board.domain.Board;
import kr.co.pawpaw.domainrdb.board.service.command.BoardCommand;
import kr.co.pawpaw.domainrdb.board.service.query.BoardQuery;
import kr.co.pawpaw.domainrdb.user.domain.User;
import kr.co.pawpaw.domainrdb.user.domain.UserId;
import kr.co.pawpaw.domainrdb.user.service.query.UserQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class BoardService {

    private final UserQuery userQuery;
    private final BoardQuery boardQuery;
    private final BoardCommand boardCommand;

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
        if (board.isRemoved()) {
            throw new BoardDeleteException();
        }
        board.remove();
        return true;
    }

}
