package kr.co.pawpaw.domainrdb.chatroom.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Collection;

@Getter
@AllArgsConstructor
public class ChatroomScheduleData {
    private Long id;
    private String name;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Collection<ChatroomScheduleParticipantResponse> participants;
}
