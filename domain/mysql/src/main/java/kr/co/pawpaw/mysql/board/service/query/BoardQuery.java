package kr.co.pawpaw.mysql.board.service.query;

import kr.co.pawpaw.mysql.board.domain.Board;
import kr.co.pawpaw.mysql.board.repository.BoardRepository;
import kr.co.pawpaw.mysql.user.domain.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class BoardQuery {

    private final BoardRepository boardRepository;

    public Optional<Board> findById(Long id){
      return boardRepository.findById(id);
    }

    public Slice<Board> getBoardListWithRepliesBy(Pageable pageable){
        return boardRepository.getBoardListWithRepliesBy(pageable);
    }

    public Slice<Board> getBoardListWithRepliesByUser_UserId(Pageable pageable, UserId userId){
        return boardRepository.getBoardListWithRepliesByUser_UserId(pageable, userId);
    }
    public Slice<Board> searchBoardsByQuery(@Param("query") String query, Pageable pageable){
        return boardRepository.searchBoardsByQuery(query, pageable);
    }
}
