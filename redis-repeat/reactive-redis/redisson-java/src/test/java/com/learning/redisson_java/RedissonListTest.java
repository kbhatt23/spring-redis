package com.learning.redisson_java;

import java.util.Arrays;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;
import org.redisson.api.RListReactive;

import com.learning.redisson_java.config.RedissonConfig;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

//list means linked list and hence at as stack and queue
public class RedissonListTest extends BaseTest{

	@Test
	public void listInsertTest() {
		RListReactive<String> messages = this.reactiveClient.getList("messages", RedissonConfig.STRING_CODEC);
		
		Mono<Boolean> allInserts = messages.add("jai shree ram").then(messages.add("jai radha madhav")).then(messages.add("jai uma mahesh"));
		
		StepVerifier.create(allInserts.then(messages.readAll()))
				.expectSubscription()
				.assertNext(names -> names.containsAll(Arrays.asList("jai shree ram","jai radha madhav","jai uma mahesh")))
				.verifyComplete();
	}
}
