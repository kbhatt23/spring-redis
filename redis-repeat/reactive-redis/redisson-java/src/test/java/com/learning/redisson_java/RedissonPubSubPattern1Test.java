package com.learning.redisson_java;

import org.junit.jupiter.api.Test;
import org.redisson.api.RPatternTopicReactive;
import org.redisson.api.listener.PatternMessageListener;

import com.learning.redisson_java.config.RedissonConfig;

public class RedissonPubSubPattern1Test extends BaseTest {

	@Test
	public void listenOne() {
		String chanel1pattern = "channel-*";

		RPatternTopicReactive patternTopic = this.reactiveClient.getPatternTopic(chanel1pattern, RedissonConfig.STRING_CODEC);
		patternTopic.addListener(String.class, new PatternMessageListener<String>() {

			@Override
			public void onMessage(CharSequence pattern, CharSequence channel, String msg) {
				System.out.println("listenOne: recieved message " + msg);
			}
		}).subscribe();
		

		sleep(60000);
	}
	
	
}
