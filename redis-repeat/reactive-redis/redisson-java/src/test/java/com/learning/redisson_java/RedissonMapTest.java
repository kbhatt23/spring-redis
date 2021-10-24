package com.learning.redisson_java;

import org.junit.jupiter.api.Test;
import org.redisson.api.RMapReactive;
import org.redisson.codec.TypedJsonJacksonCodec;

import com.learning.redisson_java.config.Student;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class RedissonMapTest extends BaseTest{

	@Test
	public void insertMapTest() {
		RMapReactive<String, String> studentMap = this.reactiveClient.getMap("student-1-map");
		
		Mono<String> doOnNext = studentMap.put("name", "kanishk")
				.then(studentMap.put("age", "23"))
				.then(studentMap.get("name"))
					.doOnNext(na -> System.out.println("insertMapTest: name is "+na));
		
		StepVerifier.create(doOnNext)
		    .expectSubscription()
		    .expectNext("kanishk")
		    .verifyComplete();
		
	}
	@Test
	public void insertMapMultiValsTest() {
		RMapReactive<String, String> studentMap = this.reactiveClient.getMap("student-multi-map");
		
		Mono<String> first = studentMap.put("name", "kanishk")
				.doOnNext(na -> System.out.println("insertMapMultiValsTest: name is "+na))
				.then(studentMap.get("name"))
				;
		Mono<String> second = studentMap.put("age", "23")
				.doOnNext(age -> System.out.println("insertMapMultiValsTest: age is "+age))
				.then(studentMap.get("age"))
				;
		

		Flux<String> updatedMerge = first.concatWith(second)
				
				;
		StepVerifier.create(updatedMerge)
		    .expectSubscription()
		    .expectNext("kanishk")
		    .expectNext("23")
		    .verifyComplete();
		
	}
	
	@Test
	public void insertMapMultiStudents() {
		Student student1 = new Student("kanishk", 30);
		Student student2 = new Student("kaka", 44);
		Student student3 = new Student("xavi", 42);
		Student student4 = new Student("puyol", 42);
		
		TypedJsonJacksonCodec typedJsonJacksonCodec = new TypedJsonJacksonCodec(Integer.class, Student.class);
		RMapReactive<Integer, Student> studentMap = this.reactiveClient.getMap("students", typedJsonJacksonCodec);
		
		Mono<Student> first = studentMap.put(1, student1)
					.then(studentMap.get(1))
				.doOnNext(student -> System.out.println("student inserted "+student));
		Mono<Student> second = studentMap.put(2, student2)
				.then(studentMap.get(2))
			.doOnNext(student -> System.out.println("student inserted "+student));
		Mono<Student> third = studentMap.put(3, student3)
				.then(studentMap.get(3))
			.doOnNext(student -> System.out.println("student inserted "+student));
		Mono<Student> four = studentMap.put(4, student4)
				.then(studentMap.get(4))
			.doOnNext(student -> System.out.println("student inserted "+student));
		
		Flux<Student> allStudents = first.concatWith(second).concatWith(third).concatWith(four);
		
		StepVerifier.create(allStudents)
		    .expectSubscription()
		    .expectNext(student1)
		    .expectNext(student2)
		    .expectNext(student3)
		    .expectNext(student4)
		    .verifyComplete();
		
	}
}
