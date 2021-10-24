package com.learning.redisson_java;

import org.junit.jupiter.api.Test;
import org.redisson.api.RAtomicLongReactive;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

//different class for numbers and bucket is not used
public class RedissonNumberTest extends BaseTest {

	@Test
	public void incrementTest() {
		String keyName = "number-1";
		RAtomicLongReactive atomicLong = this.reactiveClient.getAtomicLong(keyName);

		Mono<Long> doOnNext = atomicLong.set(100).then(atomicLong.get())
				.doOnNext(num -> System.out.println("incrementTest: num inserted " + num))
				.then(atomicLong.incrementAndGet())
				.doOnNext(num -> System.out.println("incrementTest: num incremented to " + num))
				.then(atomicLong.incrementAndGet())
				.doOnNext(num -> System.out.println("incrementTest: num incremented to " + num))
				;
		
		
		StepVerifier.create(doOnNext)
					.expectSubscription()
					.expectNext(102l)
					.verifyComplete();
		
	}
}
