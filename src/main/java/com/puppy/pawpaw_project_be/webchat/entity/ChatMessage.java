package com.puppy.pawpaw_project_be.webchat.entity;

import antlr.debug.TraceEvent;
import lombok.*;

import javax.persistence.Entity;
import java.util.List;
@Getter
@Setter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
//@Entity
public class ChatMessage {


    public enum MessageType{
        ENTER, TALK, LEAVE
    }

    private MessageType type;
    private String roomId;
    private String sender;
    private String message;
 //   private List<String> userList;
}
