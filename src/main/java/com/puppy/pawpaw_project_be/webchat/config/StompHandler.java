package com.puppy.pawpaw_project_be.webchat.config;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class StompHandler implements ChannelInterceptor {
//jwt( user 인증 )
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        return message;
    }
}
