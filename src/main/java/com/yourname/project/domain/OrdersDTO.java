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
public class OrdersDTO {
   
	private Integer order_id;
	private Integer user_id;
	private Integer funding_id;
	private Integer qty;
	private Integer amount;
	private Integer use_point;
	private Integer discount_amount;
	private String status;
	private String funding_status;
	private Date purchased_at;
	private Date refund_at;
}
