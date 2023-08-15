package kr.co.pawpaw.api.application.board;

import kr.co.pawpaw.domainrdb.board.Board;
import kr.co.pawpaw.domainrdb.board.dto.BoardDto.BoardRegisterDto;
import kr.co.pawpaw.domainrdb.board.dto.BoardDto.BoardResponseDto;
import kr.co.pawpaw.domainrdb.board.dto.BoardDto.BoardUpdateDto;
import kr.co.pawpaw.domainrdb.board.repository.BoardRepository;
import kr.co.pawpaw.domainrdb.user.domain.User;
import kr.co.pawpaw.domainrdb.user.domain.UserId;
import kr.co.pawpaw.domainrdb.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class BoardService {

    private final UserRepository userRepository;
    private final BoardRepository boardRepository;

    @Transactional
    public BoardResponseDto register(UserId userId, BoardRegisterDto registerDto) {
        if (StringUtils.hasText(registerDto.getTitle()) && StringUtils.hasText(registerDto.getContent())) {

            User user = userRepository.findById(userId).orElseThrow(EntityNotFoundException::new);

            Board board = Board.createBoard(registerDto, user);

            boardRepository.save(board);

            return BoardResponseDto.builder()
                    .title(board.getTitle())
                    .content(board.getContent())
                    .writer(user.getNickname())
                    .createdAt(LocalDateTime.now())
                    .build();
        }
        return null;
    }

    @Transactional
    public BoardUpdateDto update(UserId userId, Long id, BoardUpdateDto updateDto) {

        User user = userRepository.findById(userId).orElseThrow(EntityNotFoundException::new);
        Board board = boardRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        if (user.getUserId() != board.getUser().getUserId()) {
            return null;
        }

        if (updateDto.getTitle() != null || updateDto.getContent() != null){
            board.updateTitleAndContent(updateDto.getTitle(), updateDto.getContent());
        }




        return BoardUpdateDto.builder()
                .title(board.getTitle())
                .content(board.getContent())
                .build();
    }



    @Transactional
    public void removeBoard(UserId userId, Long id) {

        User user = userRepository.findById(userId).orElseThrow(EntityNotFoundException::new);
        Board board = boardRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        if (user.getUserId() != board.getUser().getUserId()) {

        }
        boardRepository.delete(board);
    }

}
