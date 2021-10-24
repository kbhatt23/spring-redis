package com.learning.redisson_java;

import org.junit.jupiter.api.Test;
import org.redisson.api.RBucketReactive;

import com.learning.redisson_java.config.RedissonConfig;
import com.learning.redisson_java.config.Student;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class RedissonObjectBucketTest extends BaseTest{

	@Test
	public void storeObjectInBucket() {
		RBucketReactive<Student> bucket = this.reactiveClient.getBucket("object-basic-1" , RedissonConfig.JACKSON_CODEC);
		
		Mono<Student> doOnNext = bucket.set(new Student("kanishk", 30))
			.then(bucket.get())
			.doOnNext(obj -> System.out.println("storeObjectInBucket: student found "+obj))
		;
		
		Student expected = new Student("kanishk", 30);
		
		StepVerifier.create(doOnNext)
		.expectSubscription()
		.expectNext(expected)
		.verifyComplete();
	}
}
