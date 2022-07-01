package com.example.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Service;

@Service
public class RedisSubscriber implements MessageListener {

    @Autowired
    private RedisTemplate<String, RedisData> redisTemplate;

    @Override
    public void onMessage(Message message, byte[] bytes) {
        RedisSerializer<?> valueSerializer = redisTemplate.getValueSerializer();
        RedisData redisData = (RedisData) valueSerializer.deserialize(message.getBody());

        System.out.println("Data received from redis publisher");
        System.out.println("id   : " + redisData.getId());
        System.out.println("name : " + redisData.getName());
        System.out.println("type : " + redisData.getType());
        System.out.println("data : " + redisData.getData());
    }
}
