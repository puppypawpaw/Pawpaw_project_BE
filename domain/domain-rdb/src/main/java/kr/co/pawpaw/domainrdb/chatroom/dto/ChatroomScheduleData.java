package kr.co.pawpaw.domainrdb.chatroom.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Collection;

@Getter
public class ChatroomScheduleData {
    private Long id;
    private String name;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Collection<ChatroomScheduleParticipantResponse> participants;

    @QueryProjection
    public ChatroomScheduleData(
        final Long id,
        final String name,
        final String description,
        final LocalDateTime startDate,
        final LocalDateTime endDate,
        final Collection<ChatroomScheduleParticipantResponse> participants
    ) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.participants = participants;
    }
}
