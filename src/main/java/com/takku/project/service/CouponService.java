package com.takku.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.takku.project.repository.CouponRepository;

@Service
public class CouponService {

	@Autowired
	CouponRepository couponRepository;
	
}
