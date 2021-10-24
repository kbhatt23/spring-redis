package com.learning.redis.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.learning.redis.dtos.ProductDTO;
import com.learning.redis.services.ProductsService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/products")
public class ProductsController {
	

	@Autowired
	private ProductsService productsService;
	
	
	@GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<ProductDTO> findAll(){
		
		return productsService.findAll();
	}
	
	//it is a path variable and not request param
	//hence id will be manadatory
	//in case product with this id do not exist we must give 404 as path do not exist
	@GetMapping("/{id}")
	public Mono<ResponseEntity<ProductDTO>> findByID(@PathVariable int id){
		return productsService.findByID(id);
	}
	
	@PostMapping
	public Mono<ResponseEntity<ProductDTO>> create(@RequestBody Mono<ProductDTO> requestMono){
		return productsService.create(requestMono);
	}
	
	@PutMapping("/{id}")
	public Mono<ResponseEntity<ProductDTO>> update(@RequestBody Mono<ProductDTO> requestMono , @PathVariable int id){
		return productsService.update(requestMono, id);
	}
	
	@DeleteMapping("/{id}")
	public Mono<ResponseEntity<Object>> delete( @PathVariable int id){
		return productsService.delete(id);
	}
	
	@GetMapping(path="/price-range" , produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<ProductDTO> findByPriceRange(@RequestParam(name = "minPrice" , defaultValue ="0" ) double minPrice  
			, @RequestParam(name = "maxPrice" , defaultValue ="10000000" )  double maxPrice ){
		
		return productsService.findByPriceRange(minPrice, maxPrice);
	}
	
	//server sent events
	//whenver a product is created publish an event
	//lets use sink instead of tailable cursor as of now
	@GetMapping(path = "/create-events",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	Flux<ProductDTO> pushProductCreationEvent(){
		return productsService.findProductCreateStream();
	}
	
	@GetMapping(path = "/trending-products",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	Flux<List<ProductDTO>> trendingProducts(){
		return productsService.findLatestMostViewedProducts();
	}
	
	
	
	
}
