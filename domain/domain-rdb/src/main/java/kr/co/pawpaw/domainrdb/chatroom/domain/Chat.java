package kr.co.pawpaw.domainrdb.chatroom.domain;

import kr.co.pawpaw.domainrdb.common.BaseTimeEntity;
import kr.co.pawpaw.domainrdb.user.domain.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Chat extends BaseTimeEntity {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private User sender;

    @Column(nullable = false) @Enumerated(value = EnumType.STRING)
    private ChatType type;

    @Column(columnDefinition = "MEDIUMTEXT", nullable = false)
    private String data;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Chatroom chatroom;

    @Builder
    public Chat(
        final User sender,
        final ChatType type,
        final String data,
        final Chatroom chatroom
    ) {
        this.sender = sender;
        this.type = type;
        this.data = data;
        this.chatroom = chatroom;
    }
}
