package kr.co.pawpaw.mysql.chatroom.domain;

import kr.co.pawpaw.mysql.common.BaseTimeEntity;
import kr.co.pawpaw.mysql.user.domain.User;
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

    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String description;
    @Column(nullable = false)
    private LocalDateTime startDate;
    @Column(nullable = false)
    private LocalDateTime endDate;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Chatroom chatroom;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
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
