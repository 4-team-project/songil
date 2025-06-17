package com.takku.project.domain;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
  
	private Integer productId;
	private Integer storeId;
	private String productName;
	private Integer price;
	private String description;
	private Date createdAt;

}
