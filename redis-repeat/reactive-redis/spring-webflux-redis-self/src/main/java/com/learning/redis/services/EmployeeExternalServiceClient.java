package com.learning.redis.services;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.learning.redis.beans.Employee;

import reactor.core.publisher.Mono;

//mimicking external servie by storing data locally
//could be D.B call as well wh9ihc could be reactive D.B
@Service
public class EmployeeExternalServiceClient {

	//for demo we are creating storage in client itself
	private static final Map<Integer, Employee> storage ;
	
	static {
		storage = new HashMap<>();
		storage.put(1, new Employee("krishna", -1, 1));
		storage.put(2, new Employee("kanishk", 30, 2));
		storage.put(3, new Employee("sachin", 42, 3));
		storage.put(4, new Employee("debu", 24, 4));
		
	}
	//cache annotation do not work
	public Mono<Employee> findEmployeeByID(int id){
		return Mono.fromSupplier( () -> {
			sleep();
			return storage.get(id);
		});
	}

	private void sleep() {
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public Mono<Employee> createEmployee(Employee employee){
		return Mono.fromSupplier(() -> {
			sleep();
			int empId = storage.size() + 1;
			employee.setId(empId);
			storage.put(empId, employee);
			
			return employee;
		});
	}
}
