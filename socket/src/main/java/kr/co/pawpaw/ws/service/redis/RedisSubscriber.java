package kr.co.pawpaw.ws.service.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.pawpaw.ws.handler.RedisSubscribeHandler;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

@Slf4j
@Service
@AllArgsConstructor
public class RedisSubscriber implements MessageListener {
    private final ObjectMapper objectMapper;
    private final RedisSubscribeHandler subscribeHandler;

    @Override
    public void onMessage(final Message message, final byte[] pattern) {
        try {
            Class<?> clazz = getDtoClass(message.getBody());
            callSubscribeHandler(clazz, message.getBody());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Class<?> getDtoClass(final byte[] body) throws IOException, ClassNotFoundException {
        return Class.forName((String) objectMapper.readValue(body, Map.class).get("@class"));
    }

    private void callSubscribeHandler(Class<?> clazz, byte[] body) throws IOException {
        subscribeHandler.handleDto(objectMapper.readValue(body, clazz));
    }
}