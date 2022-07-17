package com.example.redis;

import example.springdata.redis.test.condition.EnabledOnRedisAvailable;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@EnabledOnRedisAvailable
class RedisDemoApplicationTests {

	@Test
	void contextLoads() {
	}

}
