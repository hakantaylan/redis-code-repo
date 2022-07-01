package com.example.redis;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.io.Serializable;

@Data
@Builder
@RedisHash("RedisData")
public class RedisData implements Serializable {
    @Id
    private String id;
    private String name;
    @Indexed
    private String type;
    private String data;
}
