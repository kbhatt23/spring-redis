package com.learning.redis.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import com.learning.redis.dtos.ProductDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(ProductDocument.TABLE_NAME)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDocument {

	@Id
	private Integer productId;

	@Column("product_name")
	private String name;
	
	@Column("product_price")
	private double price;
	
	public static final String TABLE_NAME = "products";
	
	public ProductDocument(ProductDTO productDTO) {
		this.productId=productDTO.getProductId();
		this.name = productDTO.getName();
		this.price=productDTO.getPrice();
	}
}
