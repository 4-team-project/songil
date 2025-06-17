package com.takku.project.domain;

import java.sql.Date;

public class CouponDTO {
  
	private Integer coupon_id;
	private Integer funding_id;
	private Integer user_id;
	private Integer store_id;
	private String coupon_code;
	private String use_status;
	private Integer reviewed;
	private Date created_at;
	private Date expired_at;
}
