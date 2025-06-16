package com.yourname.project.domain;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDTO {

	private Integer review_id;
	private Integer user_id;
	private Integer product_id;
	private Integer rating;
	private String content;
	private Date created_at;
}
