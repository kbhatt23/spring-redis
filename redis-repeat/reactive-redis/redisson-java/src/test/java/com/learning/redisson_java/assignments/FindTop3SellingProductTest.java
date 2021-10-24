package com.learning.redisson_java.assignments;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.redisson.api.RScoredSortedSet;
import org.redisson.client.protocol.ScoredEntry;

import com.learning.redisson_java.BaseTest;
import com.learning.redisson_java.config.RedissonConfig;

public class FindTop3SellingProductTest extends BaseTest{

	@Test
	public void findtopThree() {
		System.out.println("game started");
		RScoredSortedSet<String> scoredSortedSet = this.blockingClient.getScoredSortedSet("products",
				RedissonConfig.STRING_CODEC);
		
		while(true) {
			Collection<ScoredEntry<String>> entryRangeReversed = scoredSortedSet.entryRangeReversed(0, 2);
			
			if(entryRangeReversed != null && !entryRangeReversed.isEmpty())
				entryRangeReversed.stream()
				.forEach(product -> System.out.println("product found "+product.getValue()+"with quantity "+product.getScore()));
				
				sleep(5000);
				System.out.println("-=-==-=-=-=-=-=-=-=-=--=-=-=-=-=-=-=-=-=-=-=-=-=");
		}
	}
}
