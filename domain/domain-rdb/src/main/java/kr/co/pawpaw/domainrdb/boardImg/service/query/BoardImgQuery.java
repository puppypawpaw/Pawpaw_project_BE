package kr.co.pawpaw.domainrdb.boardImg.service.query;

import kr.co.pawpaw.domainrdb.boardImg.domain.BoardImg;
import kr.co.pawpaw.domainrdb.boardImg.repository.BoardImgRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardImgQuery {

    private final BoardImgRepository boardImgRepository;

    public List<BoardImg> findBoardImgsWithFileByBoardId(@Param("boardId") Long boardId){
        return boardImgRepository.findBoardImgsWithFileByBoardId(boardId);
    }

}
