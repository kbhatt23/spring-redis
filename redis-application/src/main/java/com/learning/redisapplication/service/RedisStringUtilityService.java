package com.learning.redisapplication.service;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisStringUtilityService {

	@Autowired
	@Qualifier("redisTemplateString")
	private RedisTemplate<String, String> redisTemplateString;
	
	public void insertString(String key,String value) {
		redisTemplateString.opsForValue().set(key, value);
		redisTemplateString.expire(key, 1, TimeUnit.MINUTES);
	}
	
	public String recieveString(String key) {
		return redisTemplateString.opsForValue().get(key);
	}
}
