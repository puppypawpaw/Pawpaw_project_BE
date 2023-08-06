package com.puppy.pawpaw_project_be.webchat.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
public class ChatRoom {

    @Id @GeneratedValue
    private Long roomId;
    private String roomName;
    private String description;

//    @ManyToOne
//    private User user; //방장
    private LocalDateTime creatAt;
}
