package com.learning.redis.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.learning.redis.beans.Employee;

@RestController
@RequestMapping("/redis/hash")
public class RedisHashController {
	
	private static final String EMPLOYEES = "employees";
	
	@Autowired
	private RedisTemplate<String, Employee> redisTemplateEmployee;
	
	@PostMapping
	public ResponseEntity<Employee> createHash(@RequestBody Employee employee){
		
		HashOperations<String, String, Employee> opsForHash = redisTemplateEmployee.opsForHash();
		
		opsForHash.putIfAbsent(EMPLOYEES, employee.getId(), employee);
		
		return new ResponseEntity<Employee>(employee, HttpStatus.CREATED);
	}
	
	@PutMapping
	public ResponseEntity<Employee> updated(@RequestBody Employee employee){
		
		HashOperations<String, String, Employee> opsForHash = redisTemplateEmployee.opsForHash();
		
		opsForHash.put(EMPLOYEES, employee.getId(), employee);
		
		return new ResponseEntity<Employee>(employee, HttpStatus.OK);
	}
	
	@GetMapping
	public List<Employee> findAll(){
		HashOperations<String, String, Employee> opsForHash = redisTemplateEmployee.opsForHash();
		
		return opsForHash.values(EMPLOYEES);
	}
	
	@GetMapping("/{employeeID}")
	public Employee find(@PathVariable String employeeID){
		HashOperations<String, String, Employee> opsForHash = redisTemplateEmployee.opsForHash();
		
		return opsForHash.get(EMPLOYEES, employeeID);
	}

	@DeleteMapping("/{employeeID}")
	public ResponseEntity<Object> remove(@PathVariable String employeeID){
		HashOperations<String, String, Employee> opsForHash = redisTemplateEmployee.opsForHash();
		
		 opsForHash.delete(EMPLOYEES, employeeID);
		 
		 return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	
}
