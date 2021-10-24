package com.learning.redis.services;


import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

import org.redisson.api.RScoredSortedSetReactive;
import org.redisson.api.RedissonReactiveClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.learning.redis.dtos.ProductDTO;
import com.learning.redis.entities.ProductDocument;
import com.learning.redis.repositories.ProductsRepository;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;
import reactor.core.publisher.Sinks.Many;

@Service
@Slf4j
public class ProductsService {

	private CacheTemplate<Integer, ProductDocument> cacheTemplate;
	
	private ProductsRepository productsRepository;

	private Many<ProductDTO> sinks;
	
	private RScoredSortedSetReactive<ProductDocument> scoredSortedSet;
	//private RScoredSortedSetReactive<Integer> scoredSortedSet;
	
	private static final String SORTED_VIEWED_PRODUCTS = "sorted-viewed-products";
	
	
	public ProductsService(RedissonReactiveClient client ,CacheTemplate<Integer, ProductDocument> cacheTemplate,ProductsRepository productsRepository) {
		this.cacheTemplate = cacheTemplate;
		this.productsRepository=productsRepository;
		sinks = Sinks.many().replay().all();
		//just store product id
		 scoredSortedSet = client.getScoredSortedSet(SORTED_VIEWED_PRODUCTS);
		
		// scoredSortedSet = client.getScoredSortedSet(SORTED_VIEWED_PRODUCTS,JsonJacksonCodec.INSTANCE);
		
	}
	
	//multiple data
	//multiple subscribers
	//hot publisher -> like shared method , for first subscriber it will be cold and for others it will be hot
	//private Sinks.Many<ProductDTO> sinks ;
	
	
	//flux mean 0 or 1 or 2 or 3...or n elements
	public Flux<ProductDTO> findAll(){
		return productsRepository.findAll()
				//added just for demo
				   // .delayElements(Duration.ofSeconds(1))
					.map(ProductDTO :: new)
				;
	}
	
	//mono means 0 or 1 elements
	//0 means 404
	public Mono<ResponseEntity<ProductDTO>> findByID(int id) {
		return cacheTemplate.get(id)
				.flatMap(productDocument -> scoredSortedSet.addScore(productDocument, 1).map(range -> productDocument))// reduces complexity
				//.flatMap(productDocumet -> scoredSortedSet.addScore(productDocumet.getProductId(), 1).map(range -> productDocument))
				.map(ProductDTO :: new)
	 			.map(productDto -> new ResponseEntity<>(productDto, HttpStatus.OK))
				//whne none of the onnext element comes then take this
				.switchIfEmpty(Mono.just(new ResponseEntity<>(HttpStatus.NOT_FOUND)));
	}
	
	//assume while creating we do not allow id to be passed
	//response dto should have the id but not the request dto
	public Mono<ResponseEntity<ProductDTO>> create(Mono<ProductDTO> requestMono){
		//start from pipeline
		return requestMono
				.doOnNext(input -> log.info("create: input requested "+input))
				.filter(request -> StringUtils.isEmpty(request.getProductId()))
				.doOnNext(input -> log.info("create: input filtered "+input))
					.map(ProductDocument :: new)
					.flatMap(productsRepository :: save)
					.map(ProductDTO :: new)
					.doOnNext(output -> log.info("create: final created data "+output))
					.doOnNext(sinks :: tryEmitNext)
					.map(saved -> new ResponseEntity<>(saved, HttpStatus.CREATED))
					.switchIfEmpty(Mono.error(() -> new IllegalArgumentException("create: can not pass id while inserting data")))
					;
			   
	}
	
	//id can not be null as it is path variable
	public Mono<ResponseEntity<ProductDTO>> update(Mono<ProductDTO> requestMono , int id){
		return 
				requestMono
				.map(ProductDocument :: new)
				.flatMap(product -> cacheTemplate.update(id, product))
			
		   .map(ProductDTO :: new)
		   
		   .map(updated -> new ResponseEntity<>(updated, HttpStatus.OK))
		   .switchIfEmpty(Mono.error(() -> new IllegalArgumentException("update: product do not exist")))
		   ;
		
	}
	
	public Mono<ResponseEntity<Object>> delete(int id) {
		return cacheTemplate.delete(id)
				    .thenReturn(new ResponseEntity<>(HttpStatus.NO_CONTENT));
	}

	public Flux<ProductDTO> findByPriceRange(double minPrice, double maxPrice) {
		return productsRepository.findByPriceBetween(minPrice, maxPrice)
				 .map(ProductDTO :: new)
				;
	}
	
	public Flux<ProductDTO> findProductCreateStream() {
		//sinks object is singleton for multiple subscribers
		if(sinks == null)
			sinks= Sinks.many().replay().all();
		
		return sinks.asFlux();
	}
	
	public Flux<List<ProductDTO>> findLatestMostViewedProducts() {
		return Flux.interval(Duration.ofSeconds(120))
				   .flatMap(l -> scoredSortedSet.entryRangeReversed(0, 2))
				   .map(productsScoreEntry -> productsScoreEntry.stream()
						   		.map(entry -> new ProductDTO(entry.getValue()))
						   		.collect(Collectors.toList()))
				   ;
				
	}
	
}
