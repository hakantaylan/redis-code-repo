package com.example.redis.lua;

import lombok.SneakyThrows;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.List;

@Component
public class RedisLuaExample {

    private final RedisTemplate<String, String> redisTemplate;

    public RedisLuaExample(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @SneakyThrows
    @PostConstruct
    public void init() {
        ValueOperations<String, String> valueOps = redisTemplate.opsForValue();
        valueOps.set("savingAcc:customer001", "100");
        valueOps.set("fdAcc:customer001", "200");
        valueOps.set("currentAcc:customer001", "300");
        List<String> keys = Collections.singletonList("customer001");
        String[] args = new String[]{"savingAcc", "fdAcc", "currentAcc"};
        Long result = redisTemplate.execute(getScript(), keys, args);
        System.out.println("Total Amount : " + result);
    }

    private static RedisScript<Long> getScript() {
        Resource scriptSource = new ClassPathResource("account_sum.lua");
        return RedisScript.of(scriptSource, Long.class);
    }
}
