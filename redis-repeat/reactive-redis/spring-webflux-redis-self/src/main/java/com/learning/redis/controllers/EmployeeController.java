package com.learning.redis.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.learning.redis.beans.Employee;
import com.learning.redis.services.EmployeeService;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

	@Autowired
	private EmployeeService employeeService;
	
	@GetMapping("/{id}")
	public Mono<Employee> findEmployee(@PathVariable int id){
		return employeeService.find(id);
	}
	
	@PostMapping
	public Mono<ResponseEntity<Employee>> createEmployee(@RequestBody Employee employee){
		return employeeService.create(employee)
				.map(emp -> new ResponseEntity<>(emp, HttpStatus.CREATED))
				;
	}
}
