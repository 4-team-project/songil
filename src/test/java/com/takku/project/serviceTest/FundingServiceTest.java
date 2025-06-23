package com.takku.project.serviceTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.*;

import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.takku.project.domain.FundingDTO;
import com.takku.project.service.FundingService;

class FundingServiceTest {

	@Mock
	private SqlSession sqlSession;

	@InjectMocks
	private FundingService fundingService;

	private final String namespace = "com.takku.project.mapper.FundingMapper.";

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	@DisplayName("전체 펀딩 조회 성공")
	void selectAllFunding_shouldReturnList() {
		List<Object> mockList = Arrays.asList(new FundingDTO(), new FundingDTO());
		when(sqlSession.selectList(namespace + "selectAllFunding")).thenReturn(mockList);

		List<FundingDTO> result = fundingService.selectAllFunding();

		assertNotNull(result);
		assertEquals(2, result.size());
		verify(sqlSession).selectList(namespace + "selectAllFunding");
	}

	@Test
	@DisplayName("펀딩 ID로 상세 조회 성공")
	void selectFundingByFundingId_shouldReturnOne() {
		int fundingId = 1;
		FundingDTO mockFunding = new FundingDTO();
		mockFunding.setFundingId(fundingId);

		when(sqlSession.selectOne(namespace + "selectFundingByFundingId", fundingId)).thenReturn(mockFunding);

		FundingDTO result = fundingService.selectFundingByFundingId(fundingId);

		assertNotNull(result);
		assertEquals(fundingId, result.getFundingId());
		verify(sqlSession).selectOne(namespace + "selectFundingByFundingId", fundingId);
	}

	@Test
	@DisplayName("펀딩 등록 성공")
	void insertFunding_shouldReturnSuccess() {
		FundingDTO funding = new FundingDTO();
		when(sqlSession.insert(namespace + "insertFunding", funding)).thenReturn(1);

		int result = fundingService.insertFunding(funding);

		assertEquals(1, result);
		verify(sqlSession).insert(namespace + "insertFunding", funding);
	}

	@Test
	@DisplayName("펀딩 수정 성공")
	void updateFunding_shouldReturnSuccess() {
		FundingDTO funding = new FundingDTO();
		when(sqlSession.update(namespace + "updateFunding", funding)).thenReturn(1);

		int result = fundingService.updateFunding(funding);

		assertEquals(1, result);
		verify(sqlSession).update(namespace + "updateFunding", funding);
	}

	@Test
	@DisplayName("펀딩 삭제 성공")
	void deleteFunding_shouldReturnSuccess() {
		int fundingId = 1;
		when(sqlSession.delete(namespace + "deleteFunding", fundingId)).thenReturn(1);

		int result = fundingService.deleteFunding(fundingId);

		assertEquals(1, result);
		verify(sqlSession).delete(namespace + "deleteFunding", fundingId);
	}
}
