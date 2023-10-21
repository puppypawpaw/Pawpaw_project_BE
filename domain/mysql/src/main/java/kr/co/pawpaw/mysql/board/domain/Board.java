package kr.co.pawpaw.mysql.board.domain;

import kr.co.pawpaw.mysql.common.BaseTimeEntity;
import kr.co.pawpaw.mysql.reply.domain.Reply;
import kr.co.pawpaw.mysql.user.domain.User;
import lombok.*;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Where(clause = "is_Removed = false")
public final class Board extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private long id;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String writer;

    private boolean isRemoved = false;

    private boolean isBookmarked = false;

    @Column(name = "liked_count")
    private int likedCount;

    @Column(name = "reply_count")
    private int replyCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Reply> reply = new ArrayList<>();

    @Builder
    public Board(String content, User user, String writer) {
        this.content = content;
        this.user = user;
        this.writer = writer;
        this.isRemoved = false;
        this.isBookmarked = false;
    }
    public void plusLikedCount(){
        this.likedCount = likedCount +1;
    }
    public void minusLikedCount(){
        this.likedCount = likedCount - 1;
    }

    public void plusReplyCount(){
        this.replyCount = replyCount +1;
    }
    public void minusReplyCount(){
        this.replyCount = replyCount - 1;
    }
    public void addBookmark(){
        this.isBookmarked = true;
    }
    public void deleteBookmark(){
        this.isBookmarked = false;
    }


    public void updateContent(String content){
        this.content = content;
    }
    public void remove() {
        this.isRemoved = true;
    }
}
