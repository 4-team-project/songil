package com.takku.project.serviceTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.takku.project.domain.UserDTO;
import com.takku.project.service.UserService;

public class UserServiceTest {

    @Mock
    private SqlSession sqlSession;

    @InjectMocks
    private UserService userService;

    private final String namespace = "com.takku.project.mapper.UserMapper.";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("사용자 등록")
    void insertUser() {
        UserDTO user = new UserDTO();
        user.setPhone("01012345678");
        user.setUserType("BUYER");

        when(sqlSession.selectOne(eq(namespace + "countByPhone"), any(Map.class))).thenReturn(0);

        int result = userService.insertUser(user);

        assertEquals(0, result);
        verify(sqlSession).insert(namespace + "insertUser", user);
    }

    @Test
    @DisplayName("사용자 등록2")
    void insertUser2() {
        UserDTO user = new UserDTO();
        user.setPhone("01012345678");
        user.setUserType("SELLER");

        when(sqlSession.selectOne(eq(namespace + "countByPhone"), any(Map.class))).thenReturn(1);

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            userService.insertUser(user);
        });

        assertEquals("이미 존재하는 사용자 번호입니다.", thrown.getMessage());
    }

    @Test
    @DisplayName("전화번호 조회")
    void selectByPhone() {
        UserDTO user = new UserDTO();
        user.setPhone("01012345678");
        user.setPassword("pass123");

        when(sqlSession.selectOne(namespace + "selectByPhone", "01012345678")).thenReturn(user);

        UserDTO result = userService.selectByPhone("01012345678", "pass123");

        assertNotNull(result);
        assertEquals(user, result);
    }

    @Test
    @DisplayName("전화번호 조회2")
    void selectByPhone2() {
        UserDTO user = new UserDTO();
        user.setPhone("01012345678");
        user.setPassword("pass123");

        when(sqlSession.selectOne(namespace + "selectByPhone", "01012345678")).thenReturn(user);

        UserDTO result = userService.selectByPhone("01012345678", "wrongpass");

        assertNull(result);
    }

    @Test
    @DisplayName("사용자ID로 조회")
    void selectByUserId() {
        UserDTO user = new UserDTO();
        user.setUserId(1);

        when(sqlSession.selectOne(namespace + "selectByUserId", 1)).thenReturn(user);

        UserDTO result = userService.selectByUserId(1);

        assertNotNull(result);
        assertEquals(1, result.getUserId());
    }

    @Test
    @DisplayName("사용자 수정")
    void updateUser() {
        UserDTO user = new UserDTO();
        when(sqlSession.update(namespace + "updateUser", user)).thenReturn(1);

        int result = userService.updateUser(user);

        assertEquals(1, result);
    }

    @Test
    @DisplayName("이메일 중복 확인")
    void countByEmail() {
        when(sqlSession.selectOne(namespace + "countByEmail", "test@example.com")).thenReturn(2);

        int result = userService.countByEmail("test@example.com");

        assertEquals(2, result);
    }

    @Test
    @DisplayName("전화번호 중복 확인")
    void countByPhone() {
        Map<String, Object> expectedMap = new HashMap<>();
        expectedMap.put("phone", "01012345678");
        expectedMap.put("userType", "BUYER");

        when(sqlSession.selectOne(eq(namespace + "countByPhone"), any(Map.class))).thenReturn(3);

        int result = userService.countByPhone("01012345678", "BUYER");

        assertEquals(3, result);
    }
}
