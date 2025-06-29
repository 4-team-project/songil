package com.takku.project.mapper;

import com.takku.project.domain.stats.AgeGenderTagDTO;
import com.takku.project.domain.stats.LabelValueDTO;
import com.takku.project.domain.stats.OrderStatsDTO;
import com.takku.project.domain.stats.PopularProductDTO;
import com.takku.project.domain.stats.ProductRePurchaseDTO;
import com.takku.project.domain.stats.TagStatsDTO;

import java.util.List;

public interface StoreStatsMapper {
	List<OrderStatsDTO> selectMonthlyOrderStats(int storeId);

	List<PopularProductDTO> selectPopularProducts(int storeId);

	List<ProductRePurchaseDTO> getTopRePurchasedProducts(int storeId);

	List<TagStatsDTO> selectTagStats(int storeId);

	List<LabelValueDTO> selectAgeDistribution();

	List<LabelValueDTO> selectGenderRatio();

	List<AgeGenderTagDTO> selectTopTagsByAgeGender();

	/** 상품별 월별 주문/매출 통계 */
	List<OrderStatsDTO> selectProductMonthlyStats(int productId);

	/** 상품별 연령대 통계 */
	List<LabelValueDTO> selectProductAgeStats(int productId);

	/** 상품별 성별 통계 */
	List<LabelValueDTO> selectProductGenderStats(int productId);
}
