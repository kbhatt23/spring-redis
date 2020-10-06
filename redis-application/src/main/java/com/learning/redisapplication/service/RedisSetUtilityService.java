package com.learning.redisapplication.service;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisSetUtilityService {

	//same tempalte can be used for lisgt,set,sortedset and also hash with certain modification
	@Autowired
	@Qualifier("redisTemplateGeneric")
	private RedisTemplate<String, Object> redisTemplateJSON;
	
	
	public void insertJSONObject(String key,Object jsonObject) {
		//adding stack behaviour -> usoing leftPush
		//using rightpush it is queue based
		redisTemplateJSON.opsForSet().add(key, jsonObject);
		redisTemplateJSON.expire(key, 10, TimeUnit.MINUTES);
	}
	
	public Object recieveAndRemoveListObject(String key) {
		//removes randmon item from set and retrieves the content
		return redisTemplateJSON.opsForSet().pop(key);
	}
	
	//range can not be used ->  as set is random
	//public List<Object> recieveRangeObjects(String key,int start,int end){
		//return redisTemplateJSON.opsForList().range(key, start, end);
	//}
	//uniqi=ue and random
	public Set<Object> recieveAll(String key){
		return redisTemplateJSON.opsForSet().members(key);
	}
	
	public long findSize(String key) {
		return redisTemplateJSON.opsForSet().size(key);
	}

}
