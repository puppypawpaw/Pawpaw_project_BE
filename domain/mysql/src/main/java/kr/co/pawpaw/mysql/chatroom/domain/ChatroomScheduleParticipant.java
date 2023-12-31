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
public class ChatroomScheduleParticipant extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private ChatroomSchedule chatroomSchedule;
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Builder
    public ChatroomScheduleParticipant(
        final ChatroomSchedule chatroomSchedule,
        final User user
    ) {
        this.chatroomSchedule = chatroomSchedule;
        this.user = user;
    }
}
