package com.puppy.pawpaw_project_be.domain.board.entity;

import com.puppy.pawpaw_project_be.domain.board.dto.BoardDto;
import com.puppy.pawpaw_project_be.domain.user.domain.User;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public final class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;
    private String title;
    private String content;

    private String writer;

    private int commentCount;

    private boolean isRemoved = false;

    @ColumnDefault("0")
    @Column(name = "view_count",nullable = false)
    private int viewCount;

    @ColumnDefault("0")
    @Column(name = "liked_count")
    private int likedCount;

    @CreationTimestamp // INSERT, UPDATE 등의 쿼리가 발생할 때, 현재 시간을 자동으로 저장
    @Column(updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @UpdateTimestamp
    @Column
    private LocalDateTime updatedAt = LocalDateTime.now();

    @OneToMany(mappedBy = "board", orphanRemoval = true)
    private Set<BoardLikes> boardLikes = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public Board(String title, String content, User user) {
        this.title = title;
        this.content = content;
        this.user = user;
    }

    public static Board createBoard(BoardDto.BoardRegisterDto registerDto, User user){
        Board board = Board.builder()
                .id(registerDto.getId())
                .title(registerDto.getTitle())
                .content(registerDto.getContent())
                .writer(user.getNickname())
                .user(user)
                .isRemoved(false)
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
}
