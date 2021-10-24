package com.learning.redisson_java;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.redisson.api.RedissonClient;
import org.redisson.api.RedissonReactiveClient;

import com.learning.redisson_java.config.RedissonConfig;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BaseTest {

	private final RedissonConfig redissonConfig = new RedissonConfig();
	
	protected RedissonReactiveClient reactiveClient;
	
	protected RedissonClient blockingClient;
	
	@BeforeAll
	public void setup() {
		blockingClient = redissonConfig.getClient();
		reactiveClient = redissonConfig.getReactiveClient();
	}
	
	@AfterAll
	public void cleanup() {
		reactiveClient.shutdown();
	}
	
	@BeforeEach
	public void refreshTest() {
		System.out.println("-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
	}
	
	public void sleep(long ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
