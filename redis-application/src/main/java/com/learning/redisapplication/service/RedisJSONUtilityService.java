package com.learning.redisapplication.service;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisJSONUtilityService {

	@Autowired
	@Qualifier("redisTemplateGeneric")
	private RedisTemplate<String, Object> redisTemplateJSON;
	
	public void insertJSONObject(String key,Object jsonObject) {
		redisTemplateJSON.opsForValue().set(key, jsonObject);
		redisTemplateJSON.expire(key, 1, TimeUnit.MINUTES);
	}
	
	public Object recieveJSONObject(String key) {
		return redisTemplateJSON.opsForValue().get(key);
	}
}
