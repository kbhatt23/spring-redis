package com.learning.redis;

import org.junit.jupiter.api.RepeatedTest;
import org.redisson.api.RAtomicDoubleReactive;
import org.redisson.api.RAtomicLongReactive;
import org.redisson.api.RedissonReactiveClient;
import org.redisson.client.codec.StringCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

//@SpringBootTest
class SpringWebfluxRedisSelfApplicationTests {

	@Autowired
	private ReactiveStringRedisTemplate reactiveStringRedisTemplate;
	
	@Autowired
	private RedissonReactiveClient redissonReactiveClient;
	
	@RepeatedTest(3)
	void lettucePerformance() {
		
		Mono<String> doOnNext = Flux.range(1, 500000)
			.flatMap(num -> reactiveStringRedisTemplate.opsForValue().increment("lettuce-integer"))
			.then(reactiveStringRedisTemplate.opsForValue().get("lettuce-integer"))
			.doOnNext(result -> System.out.println("lettucePerformance: final result "+result))
			;
		
		StepVerifier.create(doOnNext)
					.expectSubscription()
					.expectNextCount(1)
					.verifyComplete();
		
	}
	
	@RepeatedTest(3)
	void redissonPerformance() {
		
		RAtomicLongReactive atomicLong = redissonReactiveClient.getAtomicLong("redisson-integer");
		
		Mono<Long> doOnNext = Flux.range(1, 500000)
			.flatMap(num -> atomicLong.incrementAndGet())
			.then(atomicLong.get())
			.doOnNext(result -> System.out.println("redissonPerformance: final result "+result));
		
		StepVerifier.create(doOnNext)
		.expectSubscription()
		.expectNextCount(1)
		.verifyComplete();
	}

}
