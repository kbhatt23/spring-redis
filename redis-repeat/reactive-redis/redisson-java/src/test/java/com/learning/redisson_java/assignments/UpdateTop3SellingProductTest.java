package com.learning.redisson_java.assignments;

import java.util.Random;

import org.junit.jupiter.api.Test;
import org.redisson.api.RScoredSortedSet;

import com.learning.redisson_java.BaseTest;
import com.learning.redisson_java.config.RedissonConfig;

public class UpdateTop3SellingProductTest extends BaseTest {

	private static final Random RANDOM = new Random();

	@Test
	public void updatePRoductCountInfinitely() {

		RScoredSortedSet<String> scoredSortedSet = this.blockingClient.getScoredSortedSet("products",
				RedissonConfig.STRING_CODEC);
		
		while (true) {
			sleep(500);
			int index = RANDOM.nextInt(8); // return 0 to 7 we have fixed products

			String productID = "product-" + index;
			System.out.println("incrementing count for product "+productID);

			scoredSortedSet.addScore(productID, 1);

		}
	}

}
