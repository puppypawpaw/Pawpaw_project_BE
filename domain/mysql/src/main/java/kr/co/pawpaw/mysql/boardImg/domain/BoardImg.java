package kr.co.pawpaw.mysql.boardImg.domain;


import kr.co.pawpaw.mysql.board.domain.Board;
import kr.co.pawpaw.mysql.common.BaseTimeEntity;
import kr.co.pawpaw.mysql.storage.domain.File;
import lombok.*;
import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(exclude = "board")
public class BoardImg extends BaseTimeEntity {

    @Id
    private String uuid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "file_name")
    private File file;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @Builder
    public BoardImg(String uuid, File file, Board board) {
        this.uuid = uuid;
        this.board = board;
        this.file = file;
    }
}
