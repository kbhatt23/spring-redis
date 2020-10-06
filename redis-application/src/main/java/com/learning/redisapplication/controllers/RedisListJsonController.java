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
import com.learning.redisapplication.service.RedisListUtilityService;

//for demo using json object programmer
@RestController
@RequestMapping("/programmers-list")
public class RedisListJsonController {

	@Autowired
	private RedisListUtilityService service;
	
	private static final String KEY = "programmers";
	
	@PostMapping
	public Programmer insertProgrammer(@RequestBody Programmer programmer) {
		
		service.insertJSONObject(KEY, programmer);
		return programmer;
	}
	
	@GetMapping("/start/{start}/end/{end}")
	public List<Object> recieveProgrammer(@PathVariable String start , @PathVariable String end) {
		return (List<Object>)service.recieveRangeObjects(KEY, Integer.parseInt(start), Integer.parseInt(end));
	}
	
	@GetMapping
	public List<Object> recieveAllProgrammer() {
		return (List<Object>)service.recieveAll(KEY);
	}
	
	//return and remove one item from left
	@DeleteMapping
	public Programmer retrieveAndRemove() {
		return (Programmer)service.recieveAndRemoveListObject(KEY);
	}
}
