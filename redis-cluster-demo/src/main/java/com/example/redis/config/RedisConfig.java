package com.example.redis.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

import java.util.HashSet;
import java.util.Set;

@Configuration
public class RedisConfig {

    @Value("${spring.redis.cluster.nodes}")
    private String hosts;

    @Value("${spring.redis.cluster.command-timeout}")
    private int timeout;

    private int maxAttemts = 3;


    @Bean
    public JedisPoolConfig jedisPoolConfig() {

        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setBlockWhenExhausted(false);  //Setting this to false means an error will occur immediately when a client requests a connection and none are available, default is true
        jedisPoolConfig.setMaxIdle(15);  //The maximum number of idle connections that can be held in the pool,default is 8.
        jedisPoolConfig.setMinIdle(1);  //The target for the minimum number of idle connections to maintain in the pool,default is 0
        jedisPoolConfig.setMaxTotal(20);  // The maximum number of connections that can be allocated by the pool, default is 8.
        return jedisPoolConfig;

    }

    @Bean
    public JedisCluster jedisCluster() {
        String[] clusterNodes = hosts.split(",");
        Set<HostAndPort> clusters = new HashSet<>();
        for (String clusterNode : clusterNodes) {
            if (clusterNode != null && !clusterNode.isEmpty()) {
                String[] clusterNodeEndpoint = clusterNode.split(":");
                if (clusterNodeEndpoint.length == 2)
                    clusters.add(new HostAndPort(clusterNodeEndpoint[0], Integer.parseInt(clusterNodeEndpoint[1])));

            }

        }
        return new JedisCluster(clusters, timeout, maxAttemts, jedisPoolConfig());
    }
}