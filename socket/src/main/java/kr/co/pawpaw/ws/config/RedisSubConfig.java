package kr.co.pawpaw.ws.config;

import kr.co.pawpaw.ws.service.redis.RedisSubscriber;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

@Configuration
@RequiredArgsConstructor
public class RedisSubConfig {
    private final RedisSubscriber redisSubscriber;
    private final ChannelTopic chatTopic;

    @Bean
    public RedisMessageListenerContainer redisMessageListener(final RedisConnectionFactory connectionFactory) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(redisSubscriber, chatTopic);

        return container;
    }
}
