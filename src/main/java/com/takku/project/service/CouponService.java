package com.takku.project.service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.takku.project.domain.CouponDTO;
import com.takku.project.domain.FundingDTO;
import com.takku.project.mapper.CouponMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CouponService implements CouponMapper {

	@Autowired
	private SqlSession sqlSession;

	private final String namespace = "com.takku.project.mapper.CouponMapper.";

	/**
	 * 랜덤 쿠폰 코드 생성
	 */
	public String generateRandomCode() {
		return UUID.randomUUID().toString().replace("-", "").substring(0, 12).toUpperCase();
	}

	/**
	 * 펀딩 성공 시 참여 유저에게 쿠폰 자동 발급 (스케줄러 사용)
	 */
	public void issueCouponsForFunding(FundingDTO funding, LocalDate today) {
		List<Map<String, Object>> participantList = selectParticipantsWithQtyByFundingId(funding.getFundingId());

		for (Map<String, Object> row : participantList) {
			Integer userId = ((Number) row.get("userId")).intValue();
			Integer qty = ((Number) row.get("totalQty")).intValue();

			for (int i = 0; i < qty; i++) {
				CouponDTO coupon = new CouponDTO();
				coupon.setFundingId(funding.getFundingId());
				coupon.setUserId(userId);
				coupon.setStoreId(funding.getStoreId());
				coupon.setCouponCode("TK" + UUID.randomUUID().toString().substring(0, 10).toUpperCase());
				coupon.setUseStatus("미사용");
				coupon.setCreatedAt(Date.valueOf(today));
				coupon.setExpiredAt(Date.valueOf(today.plusMonths(6)));

				insertCoupon(coupon);
				log.info("쿠폰 발급 완료: userId={}, fundingId={}, index={}", userId, funding.getFundingId());
			}
		}
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

	/**
	 * 쿠폰 상태 업데이트 (사용 처리 포함)
	 */
	public int updateCouponUseStatus(String couponCode, String useStatus) {
		Map<String, Object> map = new HashMap<>();
		map.put("couponCode", couponCode);
		map.put("useStatus", useStatus);
		map.put("usedAt", new Date(System.currentTimeMillis()));
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

	@Override
	public List<Map<String, Object>> selectParticipantsWithQtyByFundingId(int fundingId) {
		return sqlSession.selectList(namespace + "selectParticipantsWithQtyByFundingId", fundingId);
	}
}
