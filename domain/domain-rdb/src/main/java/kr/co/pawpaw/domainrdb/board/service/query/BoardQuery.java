package kr.co.pawpaw.domainrdb.board.service.query;

import kr.co.pawpaw.domainrdb.board.domain.Board;
import kr.co.pawpaw.domainrdb.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardQuery {

    private final BoardRepository boardRepository;

    public Optional<Board> findById(Long id){
      return boardRepository.findById(id);
    }

    public Slice<Board> getBoardListWithRepliesBy(Pageable pageable){
        return boardRepository.getBoardListWithRepliesBy(pageable);
    }

    public Slice<Board> searchBoardsByQuery(@Param("query") String query, Pageable pageable){
        return boardRepository.searchBoardsByQuery(query, pageable);
    }
}
