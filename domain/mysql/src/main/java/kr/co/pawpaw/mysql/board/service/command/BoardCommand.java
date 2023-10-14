package kr.co.pawpaw.mysql.board.service.command;

import kr.co.pawpaw.mysql.board.domain.Board;
import kr.co.pawpaw.mysql.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardCommand {

    private final BoardRepository boardRepository;

    public Board save(Board board){
        return boardRepository.save(board);
    }
}
