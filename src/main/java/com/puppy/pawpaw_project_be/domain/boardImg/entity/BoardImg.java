package com.puppy.pawpaw_project_be.domain.boardImg.entity;

import com.puppy.pawpaw_project_be.domain.board.entity.Board;
import com.puppy.pawpaw_project_be.domain.common.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(exclude = "board")
public class BoardImg extends BaseTimeEntity implements Comparable<BoardImg> {

    @Id
    private String uuid;

    private String fileName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    private int ord;
    @Builder
    public BoardImg(String uuid, String fileName, Board board, int ord) {
        this.uuid = uuid;
        this.fileName = fileName;
        this.board = board;
        this.ord = ord;
    }

    @Override
    public int compareTo(BoardImg o) {
        return this.ord - o.ord;
    }

    public void changeBoard(Board board){
        this.board = board;
    }
}
