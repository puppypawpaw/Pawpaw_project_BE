package kr.co.pawpaw.domainrdb.boardImg.service.command;

import kr.co.pawpaw.domainrdb.boardImg.domain.BoardImg;
import kr.co.pawpaw.domainrdb.boardImg.repository.BoardImgRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardImgCommand {

    private final BoardImgRepository boardImgRepository;

    public BoardImg save(BoardImg boardImg){
        return boardImgRepository.save(boardImg);
    }
}
