package com.learning.redis.services;

import java.util.concurrent.TimeUnit;

import org.redisson.api.RMapCacheReactive;
import org.redisson.api.RMapReactive;
import org.redisson.api.RedissonReactiveClient;
import org.redisson.codec.TypedJsonJacksonCodec;
import org.springframework.stereotype.Service;

import com.learning.redis.beans.Employee;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class EmployeeService {

	private EmployeeExternalServiceClient employeeExternalServiceClient;
	
	private RMapCacheReactive<Integer, Employee> hashStorage;
	
	private  static final TypedJsonJacksonCodec typedJsonJacksonCodec = new TypedJsonJacksonCodec(Integer.class, Employee.class);
	
	public EmployeeService(EmployeeExternalServiceClient employeeExternalServiceClient , RedissonReactiveClient client) {
		this.employeeExternalServiceClient=employeeExternalServiceClient;
		hashStorage = client.getMapCache("employees", typedJsonJacksonCodec);
	}
	
	
	
	
	public Mono<Employee> create(Employee employee){
		log.info("create: Creating Employee.");
		return employeeExternalServiceClient.createEmployee(employee)
					.flatMap(emp -> hashStorage.put(emp.getId(), emp, 5, TimeUnit.SECONDS))
					.then(Mono.just(employee))
				;
	}
	
	public Mono<Employee> find(int id){
		log.info("find: finding Employee with id "+id);
		 return hashStorage.get(id)
		 			.switchIfEmpty(employeeExternalServiceClient.findEmployeeByID(id))
		 			.flatMap(employee -> hashStorage.put(id, employee,5,TimeUnit.SECONDS))
		 			.then(hashStorage.get(id))
		 			;
		 
	}
}
