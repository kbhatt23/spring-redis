package com.learning.redisapplication.service;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisHashUtilityService {

	//same tempalte can be used for lisgt,set,sortedset and also hash with certain modification
	@Autowired
	@Qualifier("redisTemplateGeneric")
	private RedisTemplate<String, Object> redisTemplateJSON;
	
	//least priority is kept at lower index as it do sorting
	public void insertJSONObject(String key,Object jsonObject, String hashPropertyName) {
		//adding stack behaviour -> usoing leftPush
		//using rightpush it is queue based
		redisTemplateJSON.opsForHash().put(key, hashPropertyName, jsonObject);
		redisTemplateJSON.expire(key, 10, TimeUnit.MINUTES);
	}
	
	public Object recieveAndRemoveListObject(String key,String hashPropertyName) {
		Object item = redisTemplateJSON.opsForHash().get(key, hashPropertyName);
		redisTemplateJSON.opsForHash().delete(key, hashPropertyName);
		return item;
	}
	
	//range can be used because of scores
	public Object recieveHAshedObject(String key,String hashKeyPropertyName){
		return redisTemplateJSON.opsForHash().get(key, hashKeyPropertyName);
		}
	//uniqi=ue and random
	public List<Object> recieveAll(String key){
		Set<Object> keys = redisTemplateJSON.opsForHash().keys(key);
		return redisTemplateJSON.opsForHash().multiGet(key, keys);
	}
	
	public long findSize(String key) {
		return redisTemplateJSON.opsForHash().size(key);
	}

}
