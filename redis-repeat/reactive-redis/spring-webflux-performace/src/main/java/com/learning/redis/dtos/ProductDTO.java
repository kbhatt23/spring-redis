package com.learning.redis.dtos;

import com.learning.redis.entities.ProductDocument;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {
	private Integer productId;

	private String name;
	
	private double price;
	
	public ProductDTO(ProductDocument productDocument) {
		this.productId=productDocument.getProductId();
		this.name=productDocument.getName();
		this.price=productDocument.getPrice();
	}
}
