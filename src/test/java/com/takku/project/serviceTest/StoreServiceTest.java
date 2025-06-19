package com.takku.project.serviceTest;

import com.takku.project.domain.StoreDTO;
import com.takku.project.service.StoreService;

import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class StoreServiceTest {

    @InjectMocks
    private StoreService storeService;

    @Mock
    private SqlSession sqlSession;

    private final String namespace = "com.takku.project.mapper.StoreMapper.";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void insertStore_shouldReturn1() {
        StoreDTO store = StoreDTO.builder()
                .storeId(1)
                .storeName("테스트상점")
                .userId(10)
                .businessNumber("123-45-67890")
                .sido("서울")
                .sigungu("강남구")
                .categoryId(1)
                .build();

        // 정확한 SQL id + 정확한 객체 사용
        when(sqlSession.insert(namespace + "insertStore", store)).thenReturn(1);

        int result = storeService.insertStore(store);

        assertEquals(1, result);
        verify(sqlSession).insert(namespace + "insertStore", store);
    }
    
    @Test
    void selectStoreById_shouldReturnStore() {
        StoreDTO expectedStore = getTestStore();

        when(sqlSession.selectOne(namespace + "selectStoreById", 1)).thenReturn(expectedStore);

        StoreDTO result = storeService.selectStoreById(1);

        assertNotNull(result);
        assertEquals("테스트상점", result.getStoreName());
        verify(sqlSession).selectOne(namespace + "selectStoreById", 1);
    }

    @Test
    void updateStore_shouldReturn1() {
        StoreDTO store = getTestStore();

        when(sqlSession.update(namespace + "updateStore", store)).thenReturn(1);

        int result = storeService.updateStore(store);

        assertEquals(1, result);
        verify(sqlSession).update(namespace + "updateStore", store);
    }

    @Test
    void deleteStore_shouldReturn1() {
        when(sqlSession.delete(namespace + "deleteStore", 1)).thenReturn(1);

        int result = storeService.deleteStore(1);

        assertEquals(1, result);
        verify(sqlSession).delete(namespace + "deleteStore", 1);
    }

    @Test
    void countByBusinessNumber_shouldReturn1() {
        String businessNumber = "123-45-67890";
        when(sqlSession.selectOne(namespace + "countByBusinessNumber", businessNumber)).thenReturn(1);

        int result = storeService.countByBusinessNumber(businessNumber);

        assertEquals(1, result);
        verify(sqlSession).selectOne(namespace + "countByBusinessNumber", businessNumber);
    }

    @Test
    void findStoreIdByUserId_shouldReturnStoreId() {
        when(sqlSession.selectOne(namespace + "selectStoreIdByUserId", 10)).thenReturn(5);

        Integer storeId = storeService.findStoreIdByUserId(10);

        assertEquals(5, storeId);
        verify(sqlSession).selectOne(namespace + "selectStoreIdByUserId", 10);
    }

    // 테스트용 샘플 데이터 생성
    private StoreDTO getTestStore() {
        return StoreDTO.builder()
                .storeId(1)
                .storeName("테스트상점")
                .userId(10)
                .businessNumber("123-45-67890")
                .sido("서울")
                .sigungu("강남구")
                .categoryId(1)
                .build();
    }
}
