package com.takku.project.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImageDTO {

	private Integer image_id;
	private Integer product_id;
	private Integer funding_id;
	private Integer review_id;
	private String image_url;
}
