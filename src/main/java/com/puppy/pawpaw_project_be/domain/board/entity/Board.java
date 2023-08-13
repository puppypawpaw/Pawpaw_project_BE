package com.puppy.pawpaw_project_be.domain.board.entity;

import com.puppy.pawpaw_project_be.domain.board.dto.BoardDto;
import com.puppy.pawpaw_project_be.domain.boardImg.entity.BoardImg;
import com.puppy.pawpaw_project_be.domain.common.BaseTimeEntity;
import com.puppy.pawpaw_project_be.domain.user.domain.User;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
public class Board extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String writer;

    private boolean isRemoved = false;

    @Column(name = "liked_count")
    private int likedCount;


    @OneToMany(mappedBy = "board", orphanRemoval = true, cascade = CascadeType.ALL)
    private Set<BoardLikes> boardLikes = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @Builder.Default
    private Set<BoardImg> imgSet = new HashSet<>();

    @Builder
    public Board(String title, String content, User user, String writer) {
        this.title = title;
        this.content = content;
        this.user = user;
        this.writer = writer;
    }

    public static Board createBoard(BoardDto.BoardRegisterDto registerDto, User user){
        Board board = Board.builder()
                .title(registerDto.getTitle())
                .content(registerDto.getContent())
                .writer(user.getNickname())
                .user(user)
                .build();
        return board;
    }

    public void plusLikedCount(){
        this.likedCount = likedCount +1;
    }
    public void minusLikedCount(){
        this.likedCount = likedCount - 1;
    }


    public void updateTitleAndContent(String title, String content){
        this.title = title;
        this.content = content;
    }

    public void remove() {
        this.isRemoved = true;
    }


    public void addImage(String uuid, String fileName){
        BoardImg boardImg = BoardImg.builder()
                .uuid(uuid)
                .fileName(fileName)
                .board(this)
                .ord(imgSet.size())
                .build();
        imgSet.add(boardImg);
    }

    public void clearImages(){
        imgSet.stream().forEach(boardImg -> boardImg.changeBoard(null));  // 관계를 끊어준다
        this.imgSet.clear();
    }

    protected Board() {
    }
}
