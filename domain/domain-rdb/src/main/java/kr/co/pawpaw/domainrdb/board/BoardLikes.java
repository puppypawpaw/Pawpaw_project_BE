package kr.co.pawpaw.domainrdb.board;

import kr.co.pawpaw.domainrdb.user.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class BoardLikes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "boardLikes_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;


    public BoardLikes(User user, Board board) {
        this.user = user;
        this.board = board;
    }
}
