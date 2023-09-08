package kr.co.pawpaw.domainredis.chatMessage.vo;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.security.SecurityProperties;

@Getter
@Setter
public class ChatRoomVO {
    private Long id;
    private String title;
    private String description;
    private String imageUrl;
    private boolean latest;
    private SecurityProperties.User user;
}
