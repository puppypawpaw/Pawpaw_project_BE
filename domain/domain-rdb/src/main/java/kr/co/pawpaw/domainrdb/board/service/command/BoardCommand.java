package kr.co.pawpaw.domainrdb.board.service.command;

import kr.co.pawpaw.domainrdb.board.domain.Board;
import kr.co.pawpaw.domainrdb.board.repository.BoardRepository;
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
