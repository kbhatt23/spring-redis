package com.learning.redis.services;

import org.redisson.api.RMapReactive;
import org.redisson.api.RedissonReactiveClient;
import org.redisson.codec.TypedJsonJacksonCodec;
import org.springframework.stereotype.Service;

import com.learning.redis.entities.ProductDocument;
import com.learning.redis.repositories.ProductsRepository;

import reactor.core.publisher.Mono;

@Service
public class ProductCacheTemplate extends CacheTemplate<Integer, ProductDocument> {

	private ProductsRepository repository;
    private RMapReactive<Integer, ProductDocument> map;

    public ProductCacheTemplate(RedissonReactiveClient client , ProductsRepository repository) {
        this.map = client.getMap(ProductDocument.TABLE_NAME, new TypedJsonJacksonCodec(Integer.class, ProductDocument.class));
        this.repository=repository;
    }

    @Override
    protected Mono<ProductDocument> getFromSource(Integer id) {
        return this.repository.findById(id);
    }

    @Override
    protected Mono<ProductDocument> getFromCache(Integer id) {
        return this.map.get(id);
    }

    @Override
    protected Mono<ProductDocument> updateSource(Integer id, ProductDocument product) {
        return this.repository.findById(id)
                              .doOnNext(p -> product.setProductId(id))
                              .flatMap(p -> this.repository.save(product));
    }

    @Override
    protected Mono<ProductDocument> updateCache(Integer id, ProductDocument product) {
        return this.map.fastPut(id, product).thenReturn(product);
    }

    @Override
    protected Mono<Void> deleteFromSource(Integer id) {
        return this.repository.deleteById(id);
    }

    @Override
    protected Mono<Void> deleteFromCache(Integer id) {
        return this.map.fastRemove(id).then();
    }
}