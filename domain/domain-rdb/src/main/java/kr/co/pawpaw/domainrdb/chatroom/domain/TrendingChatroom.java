package kr.co.pawpaw.domainrdb.chatroom.domain;

import kr.co.pawpaw.domainrdb.common.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TrendingChatroom extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Chatroom chatroom;

    @Builder
    public TrendingChatroom(final Chatroom chatroom) {
        this.chatroom = chatroom;
    }
}
