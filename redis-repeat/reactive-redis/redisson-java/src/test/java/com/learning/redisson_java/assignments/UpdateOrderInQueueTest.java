package com.learning.redisson_java.assignments;

import java.util.Random;

import org.junit.jupiter.api.Test;
import org.redisson.api.RScoredSortedSet;

import com.learning.redisson_java.BaseTest;
import com.learning.redisson_java.config.RedissonConfig;

public class UpdateOrderInQueueTest extends BaseTest{

	private static final Random RANDOM = new Random();
	@Test
	public void updateOrders() {
		int orderIndex = 1;
		
		RScoredSortedSet<Order> orderPriorityQueue = this.blockingClient.getScoredSortedSet("orders", RedissonConfig.JACKSON_CODEC);
		while(true) {
			//o for prime 1 for std and 2 for 
			int orderTypeIndex = RANDOM.nextInt(3);
			Order order = new Order("order-"+orderIndex,Order.findType(orderTypeIndex));
			orderPriorityQueue.add(order.findPriority(), order);
			orderIndex++;
			sleep(500);
		}
	}
}
