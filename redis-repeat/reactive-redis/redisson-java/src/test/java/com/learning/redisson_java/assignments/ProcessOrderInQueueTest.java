package com.learning.redisson_java.assignments;

import java.util.Random;

import org.junit.jupiter.api.Test;
import org.redisson.api.RScoredSortedSet;

import com.learning.redisson_java.BaseTest;
import com.learning.redisson_java.config.RedissonConfig;

public class ProcessOrderInQueueTest extends BaseTest{

	private static final Random RANDOM = new Random();
	@Test
	public void processOrders() {
		
		RScoredSortedSet<Order> orderPriorityQueue = this.blockingClient.getScoredSortedSet("orders", RedissonConfig.JACKSON_CODEC);
		while(true) {
			//o for prime 1 for std and 2 for 
			if(!orderPriorityQueue.isEmpty()) {
				
			Order pollFirst = orderPriorityQueue.pollFirst();
			System.out.println("processing order "+pollFirst);
			}
			sleep(200);
		}
	}
}
