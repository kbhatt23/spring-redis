package com.learning.redisapplication.service;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisListUtilityService {

	//same tempalte can be used for lisgt,set,sortedset and also hash with certain modification
	@Autowired
	@Qualifier("redisTemplateGeneric")
	private RedisTemplate<String, Object> redisTemplateJSON;
	
	
	public void insertJSONObject(String key,Object jsonObject) {
		//adding stack behaviour -> usoing leftPush
		//using rightpush it is queue based
		redisTemplateJSON.opsForList().rightPush(key, jsonObject);
		redisTemplateJSON.expire(key, 10, TimeUnit.MINUTES);
	}
	
	public Object recieveAndRemoveListObject(String key) {
		return redisTemplateJSON.opsForList().leftPop(key);
	}
	
	public List<Object> recieveRangeObjects(String key,int start,int end){
		return redisTemplateJSON.opsForList().range(key, start, end);
	}
	public List<Object> recieveAll(String key){
		return recieveRangeObjects(key, 0, -1);
	}
	
	public long findSize(String key) {
		return redisTemplateJSON.opsForList().size(key);
	}

}
