package com.takku.project.service;

import java.util.*;
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

	// 랜덤 쿠폰 코드 생성
	public String generateRandomCode() {
		return UUID.randomUUID().toString().replace("-", "").substring(0, 12).toUpperCase();
	}

	// 주문 기반 쿠폰 발급 (쿠폰코드 리턴)
	public String createCouponAndReturnCode(Integer fundingId, Integer userId, Integer storeId, Date fundingEndDate) {
		Date createdAt = new Date();
		Date expiredAt = addMonths(fundingEndDate, 6);
		String couponCode = generateRandomCode();

		CouponDTO coupon = CouponDTO.builder().fundingId(fundingId).userId(userId).storeId(storeId)
				.couponCode(couponCode).useStatus("미사용").reviewed(0).createdAt(createdAt).expiredAt(expiredAt).build();

		insertCoupon(coupon);
		return couponCode;
	}

	private Date addMonths(Date date, int months) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MONTH, months);
		return cal.getTime();
	}

	@Override
	public int insertCoupon(CouponDTO coupon) {
		return sqlSession.insert(namespace + "insertCoupon", coupon);
	}

	@Override
	public List<CouponDTO> selectCouponByUserId(Integer userId) {
		return sqlSession.selectList(namespace + "selectCouponsByUserId", userId);
	}

	@Override
	public int updateCouponUseStatus(Map<String, Object> map) {
		return sqlSession.update(namespace + "updateCouponUseStatus", map);
	}

	public int updateCouponUseStatus(String couponCode, String useStatus) {
		Map<String, Object> map = new HashMap<>();
		map.put("couponCode", couponCode);
		map.put("useStatus", useStatus);
		map.put("usedAt", new Date());
		return updateCouponUseStatus(map);
	}

	@Override
	public int updateCouponReviewed(Integer couponId) {
		return sqlSession.update(namespace + "updateCouponReviewed", couponId);
	}

	@Override
	public CouponDTO selectByCouponCode(String couponCode) {
		return sqlSession.selectOne(namespace + "selectByCouponCode", couponCode);
	}

	@Override
	public CouponDTO selectByCouponId(Integer couponId) {
		return sqlSession.selectOne(namespace + "selectByCouponId", couponId);
	}
}
