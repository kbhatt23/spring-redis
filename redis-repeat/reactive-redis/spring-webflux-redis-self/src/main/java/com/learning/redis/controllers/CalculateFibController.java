package com.learning.redis.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.learning.redis.beans.FibResponse;
import com.learning.redis.services.FibCalculatorService;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/fib")
public class CalculateFibController {

	@Autowired
	private FibCalculatorService fibCalculatorService;
	
	@GetMapping("/{position}/{name}" )
	//public Mono<FibResponse> calculateFibAtPosition(@PathVariable int position){
	public Mono<FibResponse> calculateFibAtPosition(@PathVariable int position , @PathVariable String name){
		//return fibCalculatorService.fibAtPosition(position);
		 return Mono.fromSupplier(() -> {
			long fibinacci = fibCalculatorService.calculateRecursively(position,name);
			return new FibResponse(position, fibinacci);
		});
	}
	
	@GetMapping
	public Mono<String> test(){
		return Mono.just("jai shree ram");
		
	}

	@DeleteMapping("/{positionKey}")
	public Mono<String> clearCache(@PathVariable int positionKey){
		return fibCalculatorService.clearCache(positionKey);
	}
}
