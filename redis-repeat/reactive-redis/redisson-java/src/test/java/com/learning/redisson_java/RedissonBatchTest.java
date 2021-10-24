package com.learning.redisson_java;

import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;
import org.redisson.api.BatchOptions;
import org.redisson.api.RBatchReactive;
import org.redisson.api.RListReactive;
import org.redisson.api.RSetReactive;

import reactor.test.StepVerifier;

public class RedissonBatchTest extends BaseTest{

	@Test
	public void batchTest() {
		RBatchReactive batch = this.reactiveClient.createBatch(BatchOptions.defaults());
		
		RListReactive<Integer> batchList = batch.getList("batch-list");
		RSetReactive<Integer> batchSet = batch.getSet("batch-set");
		
		IntStream.rangeClosed(1, 10000)
				.forEach(num -> {batchSet.add(num);batchSet.add(num);});
		
		StepVerifier.create(batch.execute().then(batchList.size()).doOnNext(count -> System.out.println("batchTest: count of list "+count)))
					.expectSubscription()
					.expectNext(10000)
					.verifyComplete();
	}
}
