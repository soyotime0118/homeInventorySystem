package me.mason.management.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.embedded.RedisServer;

import javax.annotation.PreDestroy;

@Configuration
public class EmbeddedRedisConfig {
    private RedisServer redisServer;

    @Bean
    public RedisServer redisServer(@Value("${spring.redis.port}") int port) {
        this.redisServer = new RedisServer(port);
        redisServer.start();
        return redisServer;
    }


    @PreDestroy
    public void stopRedis() {
        redisServer.stop();
    }
}