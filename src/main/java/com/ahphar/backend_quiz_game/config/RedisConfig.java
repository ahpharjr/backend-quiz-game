package com.ahphar.backend_quiz_game.config;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
// import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
// import org.springframework.data.redis.serializer.RedisSerializationContext;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, String> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        return template;
    }

    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory factory) {
        Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();

        cacheConfigurations.put("phases", RedisCacheConfiguration.defaultCacheConfig()
            .entryTtl(Duration.ofHours(1)));

        cacheConfigurations.put("flashcards", RedisCacheConfiguration.defaultCacheConfig()
            .entryTtl(Duration.ofHours(1)));

        return RedisCacheManager.builder(factory)
            .withInitialCacheConfigurations(cacheConfigurations)
            .build();
    }

    // @Bean
    // public RedisCacheManager cacheManager(RedisConnectionFactory factory) {
    //     RedisCacheConfiguration defaultConfig = RedisCacheConfiguration.defaultCacheConfig()
    //         .serializeValuesWith(
    //             RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer())
    //         );

    //     Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();

    //     cacheConfigurations.put("phases", defaultConfig.entryTtl(Duration.ofHours(24)));
    //     cacheConfigurations.put("flashcards", defaultConfig.entryTtl(Duration.ofHours(24)));
    //     cacheConfigurations.put("phaseLeaderboard", defaultConfig.entryTtl(Duration.ofHours(24)));

    //     return RedisCacheManager.builder(factory)
    //         .cacheDefaults(defaultConfig)
    //         .withInitialCacheConfigurations(cacheConfigurations)
    //         .build();
    // }

}
