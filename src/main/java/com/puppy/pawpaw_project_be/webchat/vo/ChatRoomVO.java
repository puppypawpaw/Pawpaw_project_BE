package com.puppy.pawpaw_project_be.webchat.vo;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
public class ChatRoomVO {
    private Long roomId;
    private String roomName;
    private String description;
    private String leader; //방장
    private LocalDateTime creatAt;
//    private int userCnt;

}
