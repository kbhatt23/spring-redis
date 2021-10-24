package com.learning.redisson_java.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.api.RedissonReactiveClient;
import org.redisson.client.codec.Codec;
import org.redisson.client.codec.StringCodec;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;

public class RedissonConfig {
	
	private static final String REDIS_URL = "redis://127.0.0.1:6379";

	public static final Codec STRING_CODEC = StringCodec.INSTANCE;
	
	public static final Codec JACKSON_CODEC = JsonJacksonCodec.INSTANCE;

	private RedissonClient redissonClient;
	
	public RedissonClient getClient() {
		if(redissonClient == null) {
			Config config = new Config();
			config.useSingleServer()
			.setAddress(REDIS_URL);
			
			redissonClient = Redisson.create(config);
		}
		return redissonClient;
	}
	
	
	public RedissonReactiveClient getReactiveClient() {
		return getClient().reactive();
	}
	
}
