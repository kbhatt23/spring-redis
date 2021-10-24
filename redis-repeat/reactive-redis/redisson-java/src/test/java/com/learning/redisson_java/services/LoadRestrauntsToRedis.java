package com.learning.redisson_java.services;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.redisson.api.GeoEntry;
import org.redisson.api.GeoUnit;
import org.redisson.api.RGeoReactive;
import org.redisson.api.RMapReactive;
import org.redisson.api.geo.GeoSearchArgs;
import org.redisson.api.geo.OptionalGeoSearch;
import org.redisson.codec.TypedJsonJacksonCodec;

import com.learning.redisson_java.BaseTest;
import com.learning.redisson_java.dtos.GeoLocation;
import com.learning.redisson_java.dtos.Restraunt;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class LoadRestrauntsToRedis extends BaseTest {
	
	 private RGeoReactive<Restraunt> geo;
	    private RMapReactive<String, GeoLocation> map;

	    @BeforeAll
	    public void setGeo(){
	        this.geo = this.reactiveClient.getGeo("restaurants", new TypedJsonJacksonCodec(Restraunt.class));
	        this.map = this.reactiveClient.getMap("us:texas", new TypedJsonJacksonCodec(String.class, GeoLocation.class));
	    }

	    @Test
	    public void add(){
	        Mono<Void> mono = Flux.fromIterable(RestrauntUtil.readFile())
	                .flatMap(r -> this.geo.add(r.getLongitude(), r.getLatitude(), r).thenReturn(r))
	                .flatMap(r -> this.map.fastPut(r.getZip(), GeoLocation.of(r.getLongitude(), r.getLatitude())))
	                .then();
	        StepVerifier.create(mono)
	                .verifyComplete();
	    }

	    @Test
	    public void search(){
	        Mono<Void> mono = this.map.get("75224")
	                .map(gl -> GeoSearchArgs.from(gl.getLongitude(), gl.getLatitude()).radius(5, GeoUnit.MILES))
	                .flatMap(r -> this.geo.search(r))
	                .flatMapIterable(Function.identity())
	                .doOnNext(System.out::println)
	                .then();

	        StepVerifier.create(mono)
	                .verifyComplete();
	    }


}
