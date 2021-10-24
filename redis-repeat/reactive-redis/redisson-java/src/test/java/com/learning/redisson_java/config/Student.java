package com.learning.redisson_java.config;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
//if using jsonjacksoncode jdk serialzation not needed
public class Student /* implements Serializable */{

	private static final long serialVersionUID = -3451021984779038563L;

	private String name;
	private int age;
}
