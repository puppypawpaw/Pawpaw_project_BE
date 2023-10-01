package kr.co.pawpaw.mysql.chatroom.domain;

import kr.co.pawpaw.mysql.common.BaseTimeEntity;
import kr.co.pawpaw.mysql.user.domain.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatroomParticipant extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Chatroom chatroom;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private User user;

    @Column(nullable = false) @Enumerated(value = EnumType.STRING)
    private ChatroomParticipantRole role;

    @Builder
    public ChatroomParticipant(
        final Chatroom chatroom,
        final User user,
        final ChatroomParticipantRole role
    ) {
        this.chatroom = chatroom;
        this.user = user;
        this.role = role;
    }

    public boolean isManager() {
        return this.role.equals(ChatroomParticipantRole.MANAGER);
    }
}
