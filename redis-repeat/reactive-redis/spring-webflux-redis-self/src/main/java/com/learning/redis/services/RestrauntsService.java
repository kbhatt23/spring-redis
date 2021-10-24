package com.learning.redis.services;

import java.util.function.Function;

import org.redisson.api.GeoUnit;
import org.redisson.api.RGeoReactive;
import org.redisson.api.RMapReactive;
import org.redisson.api.RedissonReactiveClient;
import org.redisson.api.geo.GeoSearchArgs;
import org.redisson.codec.TypedJsonJacksonCodec;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import com.learning.redis.beans.GeoLocation;
import com.learning.redis.beans.Restraunt;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Service
@Slf4j
public class RestrauntsService implements CommandLineRunner {

	private static final String GEO_ZIP_MAP_NAME = "geo-zip-map";
	private static final String RESTAURANTS_GEO_NAME = "restaurants";
	private RGeoReactive<Restraunt> geo;
	private RMapReactive<String, GeoLocation> map;
	
	 public RestrauntsService(RedissonReactiveClient reactiveClient) {
		this.geo = reactiveClient.getGeo(RESTAURANTS_GEO_NAME, new TypedJsonJacksonCodec(Restraunt.class));
        this.map = reactiveClient.getMap(GEO_ZIP_MAP_NAME, new TypedJsonJacksonCodec(String.class, GeoLocation.class));
	}

	@Override
	public void run(String... args) throws Exception {
		 Flux.fromIterable(RestrauntUtil.readFile())
	                .flatMap(r -> this.geo.add(r.getLongitude(), r.getLatitude(), r).thenReturn(r))
	                .flatMap(r -> this.map.fastPut(r.getZip(), GeoLocation.of(r.getLongitude(), r.getLatitude())))
	                .doFinally(option -> log.info("run: saved all geolocatios inside redis geo zSet."))
	                .subscribe();

	}
	
	public Flux<Restraunt> findNearByRestraunts(String zipCode){
		return this.map.get(zipCode)
			.map(geolocation -> GeoSearchArgs.from(geolocation.getLongitude(), geolocation.getLatitude()).radius(5, GeoUnit.MILES))
			.flatMap(r -> this.geo.search(r))
			.flatMapIterable(Function.identity())
			;
	}

}
