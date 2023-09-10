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
public class ChatroomParticipant extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Chatroom chatroom;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Enumerated(value = EnumType.STRING)
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
