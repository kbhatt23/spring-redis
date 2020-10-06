package com.learning.redisapplication.controllers;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.learning.redisapplication.bean.Programmer;
import com.learning.redisapplication.service.RedisSetUtilityService;

//for demo using json object programmer
@RestController
@RequestMapping("/programmers-set")
public class RedisSetJsonController {

	@Autowired
	private RedisSetUtilityService service;
	
	private static final String KEY = "programmersSet";
	
	@PostMapping
	public Programmer insertProgrammer(@RequestBody Programmer programmer) {
		
		service.insertJSONObject(KEY, programmer);
		return programmer;
	}
	
//range reading not possible in random and unique set
	//@GetMapping("/start/{start}/end/{end}")
	//public List<Object> recieveProgrammer(@PathVariable String start , @PathVariable String end) {
		//return (List<Object>)service.recieveRangeObjects(KEY, Integer.parseInt(start), Integer.parseInt(end));
	//}
	
	@GetMapping
	public Set<Object> recieveAllProgrammer() {
		return (Set<Object>)service.recieveAll(KEY);
	}
	
	//return and remove one random item from set
	@DeleteMapping
	public Programmer retrieveAndRemove() {
		return (Programmer)service.recieveAndRemoveListObject(KEY);
	}
}
