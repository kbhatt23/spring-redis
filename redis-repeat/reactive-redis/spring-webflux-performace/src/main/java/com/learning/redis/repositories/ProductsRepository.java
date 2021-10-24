package com.learning.redis.repositories;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.learning.redis.entities.ProductDocument;

import reactor.core.publisher.Flux;

@Repository
public interface ProductsRepository extends ReactiveCrudRepository<ProductDocument, Integer> {

	public Flux<ProductDocument> findByPriceBetween(double minPrice, double maxPrice);
}
