package com.learning.redisson_java;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.redisson.api.RMapCacheReactive;
import org.redisson.codec.TypedJsonJacksonCodec;

import com.learning.redisson_java.config.Student;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class RedissonMapCacheTest extends BaseTest{

	@Test
	public void insertToCache() {
		Student student1 = new Student("kanishk", 30);
		Student student2 = new Student("kaka", 44);
		TypedJsonJacksonCodec typedJsonJacksonCodec = new TypedJsonJacksonCodec(Integer.class, Student.class);
		RMapCacheReactive<Integer, Student> mapCache = this.reactiveClient.getMapCache("special-students", typedJsonJacksonCodec);
	
		Mono<Student> one = mapCache.put(1, student1, 100, TimeUnit.SECONDS);
		Mono<Student> two = mapCache.put(2, student2, 100, TimeUnit.SECONDS);
		Flux<Student> concatWith = one.concatWith(two);
		
		concatWith.subscribe();
		sleep(1100);
		
		//let one timeout 2 should remain
		StepVerifier.create(mapCache.get(1).doOnNext(s -> System.out.println("insertToCache: first student found "+s)))
		.expectSubscription()
		.expectNextCount(1)
		     .verifyComplete();
		
		StepVerifier.create(mapCache.get(2).doOnNext(s -> System.out.println("insertToCache: second student found "+s)))
		.expectSubscription()
		.expectNext(student2)
	     .verifyComplete();
		      
	}
}
