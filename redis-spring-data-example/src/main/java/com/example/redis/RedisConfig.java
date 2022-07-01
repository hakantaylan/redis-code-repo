package com.example.redis;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@Configuration
@EnableRedisRepositories
public class RedisConfig {

    @Bean
    public RedisTemplate<String, RedisData> redisTemplate(LettuceConnectionFactory lettuceConnectionFactory) {
        RedisTemplate<String, RedisData> template = new RedisTemplate<>();
        template.setConnectionFactory(lettuceConnectionFactory);
        return template;
    }

    // for subscriber
    @Bean
    MessageListenerAdapter messageListener(RedisSubscriber redisSubscriber) {
        return new MessageListenerAdapter(redisSubscriber);
    }

        @Bean
        RedisMessageListenerContainer redisContainer(LettuceConnectionFactory lettuceConnectionFactory,
                                                     MessageListenerAdapter adapter, ChannelTopic topic) {
            final RedisMessageListenerContainer container = new RedisMessageListenerContainer();
            container.setConnectionFactory(lettuceConnectionFactory);
            container.addMessageListener(adapter, topic);
            return container;
        }

    @Bean
    ChannelTopic topic() {
        return new ChannelTopic("redis-data");
    }
}
