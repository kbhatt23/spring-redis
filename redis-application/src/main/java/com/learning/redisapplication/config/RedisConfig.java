package com.learning.redisapplication.config;

import java.util.List;
import java.net.UnknownHostException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import redis.clients.jedis.JedisPoolConfig;

@Configuration
public class RedisConfig {
	@Value("${spring.redis.jedis.pool.max-idle}")
	private int maxIdle;

	@Value("${spring.redis.jedis.pool.min-idle}")
	private int minIdle;

	@Value("${spring.redis.jedis.pool.max-active}")
	private int maxTotal;

	@Bean
	public RedisConnectionFactory redisConnectionFactory() throws UnknownHostException {
		JedisPoolConfig poolConfig = new JedisPoolConfig();
		poolConfig.setMaxTotal(maxTotal);
		poolConfig.setMinIdle(minIdle);
		poolConfig.setMaxIdle(maxIdle);

		JedisConnectionFactory factory = new JedisConnectionFactory(poolConfig);
		return factory;
	}

	//this is specifically with key as stirng and value as stirng
	
	@Bean
	public RedisTemplate<String, String> redisTemplateString(RedisConnectionFactory connectionFactory) {
		RedisTemplate<String, String> template = new RedisTemplate<>();
		template.setConnectionFactory(connectionFactory);
		// template.setDefaultSerializer(new GenericJackson2JsonRedisSerializer());
		// we are assuming the hash object can have key as object and value as object
		// jackson serilzaer will convert the object to string
		// -> no arg cosntructor and setter and getters are mandatory in hash key and
		// value class
		// template.setHashKeySerializer(new GenericJackson2JsonRedisSerializer());
		// template.setValueSerializer(new GenericJackson2JsonRedisSerializer());

		// we are assuming that the key of the cache contente is always string
		StringRedisSerializer serializer = new StringRedisSerializer();
		template.setKeySerializer(serializer);
		template.setValueSerializer(serializer);
		return template;
	}
	
	//generic for json object as of now
	//can also be used by list data type or set or sorted set except hash
	@Bean
	public RedisTemplate<String, Object> redisTemplateGeneric(RedisConnectionFactory connectionFactory) {
		RedisTemplate<String, Object> template = new RedisTemplate<>();
		template.setConnectionFactory(connectionFactory);
		StringRedisSerializer serializer = new StringRedisSerializer();
		// template.setDefaultSerializer(new GenericJackson2JsonRedisSerializer());
		// we are assuming the hash object can have key as object and value as object
		// jackson serilzaer will convert the object to string
		// -> no arg cosntructor and setter and getters are mandatory in hash key and
		// value class
		//assuming key of has will always bs string
		 template.setHashKeySerializer(serializer);
		 template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());

		// we are assuming that the key of the cache contente is always string
		
		template.setKeySerializer(serializer);
		template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
		return template;
	}
	
	
	
}
