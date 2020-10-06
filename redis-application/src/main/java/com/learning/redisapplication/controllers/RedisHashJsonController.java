package com.learning.redisapplication.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.learning.redisapplication.bean.Programmer;
import com.learning.redisapplication.service.RedisHashUtilityService;

//for demo using json object programmer
@RestController
@RequestMapping("/programmers-hash")
public class RedisHashJsonController {

	@Autowired
	private RedisHashUtilityService service;
	
	private static final String KEY = "programmersHash";
	
	@PostMapping
	public Programmer insertProgrammer(@RequestBody Programmer programmer) {
		String hashPropertyName = programmer.getName()+":"+programmer.getAge();
		service.insertJSONObject(KEY, programmer, hashPropertyName);
		return programmer;
	}
	
	@GetMapping("/{hashPropertyName}")
	public Programmer recieveProgrammer(@PathVariable String hashPropertyName ) {
		return (Programmer)service.recieveHAshedObject(KEY, hashPropertyName);
	}
	
	@GetMapping
	public List<Object> recieveAllProgrammer() {
		return (List<Object>)service.recieveAll(KEY);
	}
	
	//return and remove one item from left
	@DeleteMapping("/{hashPropertyName}")
	public Programmer retrieveAndRemove(@PathVariable String hashPropertyName ) {
		return (Programmer)service.recieveAndRemoveListObject(KEY, hashPropertyName);
	}
}
