package com.learning.redis.services;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.learning.redis.beans.FibResponse;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class FibCalculatorService {

	public Mono<FibResponse> fibAtPosition(int position) {

		return Mono.fromSupplier(() -> calculate(position));
	}

	private FibResponse calculate(int position) {
		FibResponse fibResponse = new FibResponse();
		fibResponse.setPosition(position);
		if (position == 0 || position == 1) {
			fibResponse.setFibonacci(position);
			return fibResponse;
		}

		long previous = 1;
		long previosToPrevious = 0;
		long result = 0;
		for (int i = 2; i <= position; i++) {
			result = previosToPrevious + previous;
			previosToPrevious = previous;
			previous = result;
		}

		fibResponse.setFibonacci(result);
		return fibResponse;
	}

	// for demo of cache using recursive method
	@Cacheable(cacheNames = "math:fib" , key = "#position")
	//key of has will be math:fib
	//key of entry will be method args combination and value of entry will be long return of method
	//if one fo the variable changes then cache key will be ignored
	
	//public long calculateRecursively(int position) {
	public long calculateRecursively(int position , String name) {
		log.info("calculateRecursively: Calculating Fib for position "+position);
		return fibonacci(position);
	}

	private long fibonacci(int position) {
		if (position == 0 || position == 1) {
			return position;
		}

		return fibonacci(position - 1) + fibonacci(position - 2);
	}
	
	@CacheEvict(cacheNames = "math:fib" , key = "#positionKey")
	public Mono<String> clearCache(int positionKey) {
		return Mono.just("successfully cleared cache with key "+positionKey);
	}
	
	//schedule this method every 10 seconds
	@Scheduled(fixedRate = 10000)
	@CacheEvict(cacheNames = "math:fib" , allEntries = true)
	public void scheduledClearCache() {
		log.info("scheduledClearCache: clearing scheduled cache.....");
	}

}
