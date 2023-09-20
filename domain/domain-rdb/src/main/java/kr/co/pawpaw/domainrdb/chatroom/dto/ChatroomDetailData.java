package kr.co.pawpaw.domainrdb.chatroom.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class ChatroomDetailData {
    private Long id;
    private String name;
    private String description;
    private String coverUrl;
    private LocalDateTime lastChatTime;
    private List<String> hashTagList;
    private String managerName;
    private String managerImageUrl;
    private Long participantNumber;
    private Boolean hasNotice;
    private Boolean hasSchedule;

    @QueryProjection
    public ChatroomDetailData(
        final Long id,
        final String name,
        final String description,
        final String coverUrl,
        final LocalDateTime lastChatTime,
        final List<String> hashTagList,
        final String managerName,
        final String managerImageUrl,
        final Long participantNumber,
        final Boolean hasNotice,
        final Boolean hasSchedule
    ) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.coverUrl = coverUrl;
        this.lastChatTime = lastChatTime;
        this.hashTagList = hashTagList;
        this.managerName = managerName;
        this.managerImageUrl = managerImageUrl;
        this.participantNumber = participantNumber;
        this.hasNotice = hasNotice;
        this.hasSchedule = hasSchedule;
    }
}
