package com.learning.redisson_java;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.redisson.api.RBucketReactive;

import com.learning.redisson_java.config.RedissonConfig;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class RedissonStringTest extends BaseTest{

	@Test
	public void setAndgetString() {
		
		RBucketReactive<String> bucket = reactiveClient.getBucket("user-1"
				,RedissonConfig.STRING_CODEC
				);
		
		String value = "krishna";
		Mono<String> returnRes =  bucket.set(value)
		    .then(bucket.get())
		    .doOnNext(name -> System.out.println("setAndgetString: name found "+name))
		    ;
		  
		StepVerifier.create(returnRes)
		    .expectNext(value)
		    .verifyComplete();
		
	}
	
	@Test
	public void setAndgetStringWithExpiry() {
		String message = "hello world";
		RBucketReactive<String> bucket = reactiveClient.getBucket("user-expiry"
				,RedissonConfig.STRING_CODEC
				);
		      Mono<String> getSetMono = bucket
				.set(message, 10, TimeUnit.SECONDS)
				.then(bucket.get())
				.doOnNext(msg -> System.out.println("setAndgetStringWithExpiry: message is "+msg))
				;
		
		      StepVerifier.create(getSetMono)
		      .expectNext(message)
		      		.verifyComplete();
		
	}
	
	@Test
	public void setAndgetStringWithExpiryValidation() {
		RBucketReactive<String> bucket = reactiveClient.getBucket("deletable-message", RedissonConfig.STRING_CODEC);
		
		String message = "hell yeah";
		Mono<String> doOnNext = bucket.set(message, 1, TimeUnit.SECONDS)
		.then(bucket.get())
		.doOnNext(msg -> System.out.println("setAndgetStringWithExpiryValidation: message is "+msg))
		;
		//message should be there till now
		 StepVerifier.create(doOnNext)
		 .expectSubscription()
	      .expectNext(message)
	      		.verifyComplete();
		
		sleep(1500);
		//should be expired by now
		Mono<String> doOnNext2 = bucket.get()
			.doOnNext(res -> System.out.println("setAndgetStringWithExpiryValidation: after ttl message "+res))
			;
		
		//data should not come
		//mono should directly return oncomplete event
		StepVerifier.create(doOnNext2)
		.expectSubscription()
		//ttl over so data is deleted hence direct oncomplete
		//.expectNext(message)
		.verifyComplete();
	}
}
