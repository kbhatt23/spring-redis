package com.learning.redis;


import java.nio.charset.StandardCharsets;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.Resource;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import com.learning.redis.entities.ProductDocument;
import com.learning.redis.repositories.ProductsRepository;

import reactor.core.publisher.Flux;

//disable this for postgres
//for postgres we create table manually from pgadmin
@Service
public class InsertSampleProducts implements CommandLineRunner{
	
	  @Value("classpath:schema.sql")
	    private Resource initSql;

	    @Autowired
	    private R2dbcEntityTemplate entityTemplate;
	    
	    @Autowired
	    private ProductsRepository productsRepository;

	    @Override
	    public void run(String... args) throws Exception {
	        String query = StreamUtils.copyToString(initSql.getInputStream(), StandardCharsets.UTF_8);
	        System.out.println("InsertSampleProducts.run: sql query to execute: "+query);
	        
	        Flux<ProductDocument> createProductsFlux = Flux.range(1, 1000)
	        	.map(i -> new ProductDocument(null, "product-"+i, ThreadLocalRandom.current().nextDouble(1,100)))
	        	.collectList()
	        	.flatMapMany(productsRepository :: saveAll)
	        	;	        
	        this.entityTemplate
	                .getDatabaseClient()
	                .sql(query)
	                .then()
	                .thenMany(createProductsFlux)
	                .doFinally(s -> System.out.println("InsertSampleProducts.run: Products inserted succesfully."))
	                .subscribe();

	    }

//	@Autowired
//	private UsersRepository usersRepository;
//	
//	@Override
//	public void run(String... args) throws Exception {
//		log.info("run: Inserting users in Mongo DB");
//		
//		//just create users with no transaction
//		usersRepository.deleteAll()
//				.doOnSuccess(voidRes -> log.info("run: Removed old users"))
//		        .thenMany(Flux.range(1, 5))
//		        .map(Long::valueOf)
//		        .map(id -> new UserEntity(id, "user-"+id, id*100d, null))
//		        .flatMap(usersRepository :: save)
//		        .blockLast()
////		        .subscribe(saved -> log.info("run: saved user "+saved), 
////		        		err ->{},
////		        		() -> log.info("run: Sample users inserted succesfully")
////		        		);
//		        ;
//		
//	}

}
