package com.learning.redisapplication.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.learning.redisapplication.service.RedisStringUtilityService;

@RestController
@RequestMapping("/strings")
public class RedisStringValueController {

	@Autowired
	private RedisStringUtilityService service;
	
	@GetMapping("/send/{key}/{value}")
	public void sendString(@PathVariable String key, @PathVariable String value) {
		service.insertString(key, value);
	}
	
	@GetMapping("/recieve/{key}")
	public String getString(@PathVariable String key) {
		return service.recieveString(key);
	}
}
