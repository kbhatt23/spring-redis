package com.learning.redis.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.learning.redis.beans.Restraunt;
import com.learning.redis.services.RestrauntsService;

import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/restraunts")
public class RestrauntController {

	@Autowired
	private RestrauntsService restrauntsService;
	
	@GetMapping("/nearby/{zipCode}")
	public Flux<Restraunt> findNearbyRestraunts(@PathVariable String zipCode){
		return restrauntsService.findNearByRestraunts(zipCode);
	}
}
