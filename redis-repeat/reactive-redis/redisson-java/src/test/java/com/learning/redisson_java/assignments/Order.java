package com.learning.redisson_java.assignments;

import lombok.Data;

@Data
public class Order {
	public static OrderType findType(int index) {
		
		switch (index) {
		case 0:
			return OrderType.PRIME;
		case 1:
			return OrderType.STANDARD;

		case 2:
			return OrderType.GUEST;

		default:
			throw new IllegalArgumentException("unkonwn index passed "+index);
		}
	}
	
	public int findPriority() {
		switch (orderType) {
		case PRIME:
			return 0;
		case STANDARD:
			return 1;
		case GUEST:
			return 2;
		default:
			throw new IllegalArgumentException("unkonwn ordertype passed "+orderType);
		}
	}

	public Order(String orderId, OrderType orderType) {
		super();
		this.orderId = orderId;
		this.orderType = orderType;
	}
	



	public Order() {
		super();
	}




	private String orderId;
	
	private OrderType orderType; 
}

//0,1,2
enum OrderType{
	PRIME,STANDARD,GUEST
}
