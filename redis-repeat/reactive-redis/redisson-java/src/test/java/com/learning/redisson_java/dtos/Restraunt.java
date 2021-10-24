
package com.learning.redisson_java.dtos;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Restraunt {
	private String id;
	private String city;
	private double latitude;
	private double longitude;
	private String name;
	private String zip;
}