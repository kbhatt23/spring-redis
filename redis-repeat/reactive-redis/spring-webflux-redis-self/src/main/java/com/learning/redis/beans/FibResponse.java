package com.learning.redis.beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FibResponse {

	private int position;
	
	private long fibonacci;
}
