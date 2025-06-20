package com.takku.project.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.takku.project.domain.CouponDTO;
import com.takku.project.mapper.CouponMapper;

@Service
public class CouponService implements CouponMapper {

	@Autowired
	SqlSession sqlSession;
	String namespace = "com.takku.project.mapper.CouponMapper.";
	
	@Override
	public int insertCoupon(CouponDTO coupon) {
		int result = sqlSession.insert(namespace + "insertCoupon", coupon);
		return result;
	}

	@Override
	public List<CouponDTO> selectCouponByUserId(Integer userId) {
		List<CouponDTO> couponlist = sqlSession.selectList(namespace + "selectCouponByUserId", userId);
		return couponlist;
	}

	@Override
	public int updateCouponUseStatus(String couponCode, String useStatus) {
		Map<String, Object> map = new HashMap<>();
		map.put("couponCode", couponCode);
		map.put("useStatus", useStatus);
		return sqlSession.update(namespace + "updateCouponUseStatus", map); 
	}

	@Override
	public int updateCouponReviewed(Integer couponId, Integer reviewed) {
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("couponId", couponId);
		map.put("reviewed", reviewed);
		return sqlSession.update(namespace + "updateCouponReviewed", map);
	}

	@Override
	public CouponDTO selectByCouponCode(String couponCode) {
		CouponDTO coupon = sqlSession.selectOne(namespace + "selectByCouponCode", couponCode);
		return coupon;
	}
}
