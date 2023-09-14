package kr.co.pawpaw.domainrdb.chatroom.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
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
}
