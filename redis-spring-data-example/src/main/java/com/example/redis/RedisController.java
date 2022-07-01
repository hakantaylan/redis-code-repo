package com.example.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RedisController {

    @Autowired
    private RedisTemplate<String, RedisData> redisTemplate;

    @Autowired
    private RedisDataRepository redisDataRepository;

    @PostMapping("/redis/pub")
    public ResponseEntity push(@RequestBody RedisData redisData) {
        String pubSubChannel = "redis-data";
        redisTemplate.convertAndSend(pubSubChannel, redisData);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/redis/save")
    public ResponseEntity save(@RequestBody RedisData redisData) {
        redisDataRepository.save(redisData);
        return ResponseEntity.ok().build();
    }

    @GetMapping("redis/read/type/{type}")
    public List<RedisData> readByType(@PathVariable String type) {
        return redisDataRepository.findByType(type);
    }

    @GetMapping("redis/read/id/{id}")
    public RedisData readById(@PathVariable String id) {
        return redisDataRepository.findById(id).get();
    }

    @GetMapping("/ping")
    public void ping() {
        System.out.println("pong");
    }


}
