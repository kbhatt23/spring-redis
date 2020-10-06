package com.learning.redisapplication.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.learning.redisapplication.bean.Programmer;
import com.learning.redisapplication.service.RedisJSONUtilityService;

//for demo using json object programmer
@RestController
@RequestMapping("/programmers")
public class RedisJsonController {

	@Autowired
	private RedisJSONUtilityService service;
	
	@PostMapping
	public Programmer insertProgrammer(@RequestBody Programmer programmer) {
		String key = programmer.getName()+":"+programmer.getAge()+":"+programmer.getCompany();
		service.insertJSONObject(key, programmer);
		return programmer;
	}
	
	@GetMapping("/{key}")
	public Programmer recieveProgrammer(@PathVariable String key) {
		return (Programmer)service.recieveJSONObject(key);
	}
	
}
