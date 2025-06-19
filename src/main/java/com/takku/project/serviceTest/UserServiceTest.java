package com.takku.project.serviceTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.BeforeEach;
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
    void insertUser_shouldInsertIfPhoneNotExist() {
        UserDTO user = new UserDTO();
        user.setPhone("01012345678");
        user.setUserType("BUYER");

        when(sqlSession.selectOne(eq(namespace + "countByPhone"), any(Map.class))).thenReturn(0);
        //when(sqlSession.insert(namespace + "insertUser", user)).thenReturn(1);

        int result = userService.insertUser(user);

        assertEquals(0, result);
        verify(sqlSession).insert(namespace + "insertUser", user);
    }

    @Test
    void insertUser_shouldThrowExceptionIfPhoneExists() {
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
    void selectByPhone_shouldReturnUserIfPasswordMatches() {
        UserDTO user = new UserDTO();
        user.setPhone("01012345678");
        user.setPassword("pass123");

        when(sqlSession.selectOne(namespace + "selectByPhone", "01012345678")).thenReturn(user);

        UserDTO result = userService.selectByPhone("01012345678", "pass123");

        assertNotNull(result);
        assertEquals(user, result);
    }

    @Test
    void selectByPhone_shouldReturnNullIfPasswordDoesNotMatch() {
        UserDTO user = new UserDTO();
        user.setPhone("01012345678");
        user.setPassword("pass123");

        when(sqlSession.selectOne(namespace + "selectByPhone", "01012345678")).thenReturn(user);

        UserDTO result = userService.selectByPhone("01012345678", "wrongpass");

        assertNull(result);
    }

    @Test
    void selectByUserId_shouldReturnUser() {
        UserDTO user = new UserDTO();
        user.setUserId(1);

        when(sqlSession.selectOne(namespace + "selectByUserId", 1)).thenReturn(user);

        UserDTO result = userService.selectByUserId(1);

        assertNotNull(result);
        assertEquals(1, result.getUserId());
    }

    @Test
    void updateUser_shouldReturnUpdateResult() {
        UserDTO user = new UserDTO();
        when(sqlSession.update(namespace + "updateUser", user)).thenReturn(1);

        int result = userService.updateUser(user);

        assertEquals(1, result);
    }

    @Test
    void countByEmail_shouldReturnCount() {
        when(sqlSession.selectOne(namespace + "countByEmail", "test@example.com")).thenReturn(2);

        int result = userService.countByEmail("test@example.com");

        assertEquals(2, result);
    }

    @Test
    void countByPhone_shouldReturnCount() {
        Map<String, Object> expectedMap = new HashMap<>();
        expectedMap.put("phone", "01012345678");
        expectedMap.put("userType", "BUYER");

        when(sqlSession.selectOne(eq(namespace + "countByPhone"), any(Map.class))).thenReturn(3);

        int result = userService.countByPhone("01012345678", "BUYER");

        assertEquals(3, result);
    }
}
