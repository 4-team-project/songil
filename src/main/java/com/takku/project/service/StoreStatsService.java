package com.takku.project.service;

import com.takku.project.domain.stats.AgeGenderTagDTO;
import com.takku.project.domain.stats.LabelValueDTO;
import com.takku.project.domain.stats.OrderStatsDTO;
import com.takku.project.domain.stats.PopularProductDTO;
import com.takku.project.domain.stats.ProductRePurchaseDTO;
import com.takku.project.domain.stats.TagStatsDTO;
import com.takku.project.mapper.StoreStatsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StoreStatsService {

	@Autowired
	private StoreStatsMapper statsMapper;

	public List<OrderStatsDTO> getMonthlyOrderStats(int storeId) {
		return statsMapper.selectMonthlyOrderStats(storeId);
	}

	public List<PopularProductDTO> getPopularProducts(int storeId) {
		return statsMapper.selectPopularProducts(storeId);
	}

	public List<ProductRePurchaseDTO> getTopRePurchasedProducts(int storeId) {
		return statsMapper.getTopRePurchasedProducts(storeId);
	}

	public List<TagStatsDTO> getTagStats(int storeId) {
		return statsMapper.selectTagStats(storeId);
	}

	public List<LabelValueDTO> getAgeDistribution() {
		List<LabelValueDTO> rawData = statsMapper.selectAgeDistribution();
		double total = rawData.stream().mapToDouble(LabelValueDTO::getValue).sum();

		for (LabelValueDTO item : rawData) {
			double percent = (item.getValue() * 100.0) / total;
			item.setLabel(item.getLabel() + " (" + String.format("%.1f", percent) + "%)");
			item.setValue(percent); // 이제 실수값 그대로 유지
		}
		return rawData;
	}

	public List<LabelValueDTO> getGenderRatio() {
		List<LabelValueDTO> rawData = statsMapper.selectGenderRatio();
		double total = rawData.stream().mapToDouble(LabelValueDTO::getValue).sum();

		for (LabelValueDTO item : rawData) {
			double percent = (item.getValue() * 100.0) / total;
			item.setLabel(item.getLabel() + " (" + String.format("%.1f", percent) + "%)");
			item.setValue(percent);
		}
		return rawData;
	}

	public List<AgeGenderTagDTO> getTopTagsByAgeGender() {
		return statsMapper.selectTopTagsByAgeGender();
	}
}
