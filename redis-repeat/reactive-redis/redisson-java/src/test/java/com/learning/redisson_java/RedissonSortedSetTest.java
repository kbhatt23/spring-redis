package com.learning.redisson_java;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;
import org.redisson.api.RScoredSortedSetReactive;

import com.learning.redisson_java.config.RedissonConfig;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class RedissonSortedSetTest extends BaseTest{

	@Test
	public void sortedSetInsert() {
		
		RScoredSortedSetReactive<String> scoredSortedSet = this.reactiveClient.getScoredSortedSet("sorted-messages", RedissonConfig.STRING_CODEC);
	
		
		 Flux<Collection<String>> allData = Flux.range(1, 10)
			.flatMap(num -> scoredSortedSet.add(num, "jai shree ram "+num))
			.thenMany(scoredSortedSet.readAll())
			.doOnNext(score -> System.out.println("final scored card is: "+score))
			;
		
		StepVerifier.create(allData)
				.expectSubscription()
				.expectNext(IntStream.rangeClosed(1, 10).mapToObj(i -> "jai shree ram "+i).collect(Collectors.toList()))
				.verifyComplete();
				
				;
		
	}
}
