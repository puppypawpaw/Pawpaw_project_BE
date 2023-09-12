package kr.co.pawpaw.domainrdb.boardImg.domain;


import kr.co.pawpaw.domainrdb.board.domain.Board;
import kr.co.pawpaw.domainrdb.common.BaseTimeEntity;
import lombok.*;
import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(exclude = "board")
public class BoardImg extends BaseTimeEntity {

    @Id
    private String uuid;
    private String fileName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @Builder
    public BoardImg(String uuid, String fileName, Board board) {
        this.uuid = uuid;
        this.fileName = fileName;
        this.board = board;
    }
}
