package com.example.redis.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.ReadMode;
import org.redisson.connection.balancer.RoundRobinLoadBalancer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.HostAndPort;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Configuration
public class RedissonConfig {

    @Value("${spring.redis.cluster.nodes}")
    private String hosts;

    @Value("${spring.redis.cluster.command-timeout}")
    private int timeout;

    private int maxAttemts = 3;


    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        String[] clusterNodes = hosts.split(",");
        List<String> nodeAddresses = new LinkedList<>();
        for (String clusterNode : clusterNodes) {
            if (clusterNode != null && !clusterNode.isEmpty()) {
                nodeAddresses.add("redis://" + clusterNode);
            }

        }
        config.useClusterServers()
                .setReadMode(ReadMode.SLAVE)
                .setLoadBalancer(new RoundRobinLoadBalancer())
                .setConnectTimeout(3_000)
                .setNodeAddresses(nodeAddresses);
        return Redisson.create(config);
    }
}