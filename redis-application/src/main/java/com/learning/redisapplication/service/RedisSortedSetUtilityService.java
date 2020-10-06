package com.learning.redisapplication.service;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisSortedSetUtilityService {

	//same tempalte can be used for lisgt,set,sortedset and also hash with certain modification
	@Autowired
	@Qualifier("redisTemplateGeneric")
	private RedisTemplate<String, Object> redisTemplateJSON;
	
	//least priority is kept at lower index as it do sorting
	public void insertJSONObject(String key,Object jsonObject, double priority) {
		//adding stack behaviour -> usoing leftPush
		//using rightpush it is queue based
		redisTemplateJSON.opsForZSet().add(key, jsonObject, priority);
		redisTemplateJSON.expire(key, 10, TimeUnit.MINUTES);
	}
	
	public Object recieveAndRemoveListObject(String key) {
		//removing first item from sorted set
		Set<Object> rangeObject = redisTemplateJSON.opsForZSet().range(key, 0, 0);
		Object item  = rangeObject.stream().findFirst().orElse(null);
		redisTemplateJSON.opsForZSet().removeRange(key, 0, 0);
		return item;
	}
	
	//range can be used because of scores
	public Set<Object> recieveRangeObjects(String key,int start,int end){
		return redisTemplateJSON.opsForZSet().range(key, start, end);
		}
	//uniqi=ue and random
	public Set<Object> recieveAll(String key){
		return recieveRangeObjects(key, 0, -1);
	}
	
	public long findSize(String key) {
		return redisTemplateJSON.opsForZSet().zCard(key);
	}

}
