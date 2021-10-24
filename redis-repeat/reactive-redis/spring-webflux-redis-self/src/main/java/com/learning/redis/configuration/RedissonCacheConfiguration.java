package com.learning.redis.configuration;

import org.redisson.api.RedissonClient;
import org.redisson.spring.cache.RedissonSpringCacheManager;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableCaching
@EnableScheduling
public class RedissonCacheConfiguration {

	@Bean
	public CacheManager cacheManager(RedissonClient redissonClient) {
		return new RedissonSpringCacheManager(redissonClient);
	}
}
