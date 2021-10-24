package com.learning.redisson_java;

import org.junit.jupiter.api.Test;
import org.redisson.api.RHyperLogLogReactive;
import org.redisson.api.RSetReactive;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class RedissonHyperlogLogTest extends BaseTest{

	@Test
	public void testCount() {
		RHyperLogLogReactive<String> hyperLogLog = this.reactiveClient.getHyperLogLog("ips");
		
		Mono<Boolean> one = hyperLogLog.add("123.124.125.1");
		Mono<Boolean> two = hyperLogLog.add("123.124.125.2");
		Mono<Boolean> three = hyperLogLog.add("123.124.125.3");
		Mono<Boolean> four = hyperLogLog.add("123.124.125.1");
		Mono<Boolean> five = hyperLogLog.add("123.124.125.2");
		Mono<Boolean> six = hyperLogLog.add("123.124.125.5");
		Mono<Boolean> seven = hyperLogLog.add("123.124.125.7");
		
		Mono<Long> expectedCountMono = one.then(two).then(three).then(four).then(five).then(six).then(seven)
			.then(hyperLogLog.count())
		;
		
		StepVerifier.create(expectedCountMono)
					.expectSubscription()
					.expectNext(5l)
					.verifyComplete();
	}
	
	@Test
	public void testCountNumbers() {
		RHyperLogLogReactive<Integer> hyperLogLog = this.reactiveClient.getHyperLogLog("numbers");
		
		Mono<Long> countHuge = Flux.range(1, 50000)
			 .flatMap(number -> hyperLogLog.add(number))
			 .then(hyperLogLog.count())
			 ;
		
		StepVerifier.create(countHuge)
				.expectSubscription()
				.consumeNextWith(count -> System.out.println("testCountNumbers: count of unique numbes "+count))
				.verifyComplete();
	}
	
	@Test
	public void testCountNumbersUsingSet() {
		RSetReactive<Object> unqiueNumbersSet = this.reactiveClient.getSet("numbersSet");
		
		Mono<Integer> countHuge = Flux.range(1, 50000)
			 .flatMap(number -> unqiueNumbersSet.add(number))
			 .then(unqiueNumbersSet.size())
			 ;
		
		StepVerifier.create(countHuge)
				.expectSubscription()
				.consumeNextWith(count -> System.out.println("testCountNumbersUsingSet: count of unique numbes "+count))
				.verifyComplete();
	}
}
