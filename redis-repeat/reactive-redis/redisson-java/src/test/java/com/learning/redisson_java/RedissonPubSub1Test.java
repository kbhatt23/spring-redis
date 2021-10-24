package com.learning.redisson_java;

import org.junit.jupiter.api.Test;
import org.redisson.api.RTopicReactive;
import org.redisson.api.listener.MessageListener;

import com.learning.redisson_java.config.RedissonConfig;

public class RedissonPubSub1Test extends BaseTest {

	@Test
	public void listenOne() {
		String chanel1Name = "channel-1";

		RTopicReactive topic1 = this.reactiveClient.getTopic(chanel1Name, RedissonConfig.STRING_CODEC);

		topic1.addListener(String.class, new MessageListener<String>() {

			@Override
			public void onMessage(CharSequence channel, String msg) {
				System.out.println("listenOne: recieved message " + msg);
			}
		})
		.subscribe()
		;

		sleep(60000);
	}
	
	
}
