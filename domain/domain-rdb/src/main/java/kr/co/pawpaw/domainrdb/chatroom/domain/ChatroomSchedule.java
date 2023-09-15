package kr.co.pawpaw.domainrdb.chatroom.domain;

import kr.co.pawpaw.domainrdb.common.BaseTimeEntity;
import kr.co.pawpaw.domainrdb.user.domain.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatroomSchedule extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    @ManyToOne(fetch = FetchType.LAZY)
    private Chatroom chatroom;
    @ManyToOne(fetch = FetchType.LAZY)
    private User creator;

    @Builder
    public ChatroomSchedule(
        final String name,
        final String description,
        final LocalDateTime startDate,
        final LocalDateTime endDate,
        final Chatroom chatroom,
        final User creator
    ) {
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.chatroom = chatroom;
        this.creator = creator;
    }
}
