// package com.ahphar.backend_quiz_game.config;

// import java.time.Duration;
// import java.util.HashMap;
// import java.util.Map;

// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.data.redis.cache.RedisCacheConfiguration;
// import org.springframework.data.redis.cache.RedisCacheManager;
// import org.springframework.data.redis.connection.RedisConnectionFactory;

// @Configuration
// public class RedisCacheConfig {

//     @Bean
//     public RedisCacheManager cacheManager(RedisConnectionFactory factory) {
//         RedisCacheConfiguration leaderboardCache = RedisCacheConfiguration.defaultCacheConfig()
//                 .entryTtl(Duration.ofMinutes(5)) // refresh every 5 mins
//                 .disableCachingNullValues();

//         Map<String, RedisCacheConfiguration> cacheConfigs = new HashMap<>();
//         cacheConfigs.put("phaseLeaderboard", leaderboardCache);

//         return RedisCacheManager.builder(factory)
//                 .withInitialCacheConfigurations(cacheConfigs)
//                 .build();
//     }
// }
