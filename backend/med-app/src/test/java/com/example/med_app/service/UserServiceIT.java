package com.example.med_app.service;

import com.example.med_app.dto.response.UserResponseDTO;
import com.example.med_app.entity.User;
import com.example.med_app.enums.Gender;
import com.example.med_app.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class UserServiceIT {

    @Container
    @ServiceConnection
    static GenericContainer<?> redis =  new GenericContainer<>(DockerImageName.parse("redis:latest"))
            .withExposedPorts(6379);
            //.withCommand("redis-server", "--requirepass", "TEST_PASSWORD");

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(DockerImageName.parse("postgres:17"))
            .withDatabaseName("dental_clinic")
            .withUsername("test")
            .withPassword("test");

    @Autowired
    private UserService userService;

    @MockitoBean
    private UserRepository userRepository;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @BeforeEach
    void setup() {
        Mockito.reset(userRepository);
    }

    @Test
    void shouldReturnUserFromCacheAfterSecondCall() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        user.setFirstName("Marvin");
        user.setLastName("Branagh");
        user.setPassword("1998");
        user.setEmail("marvin@test.com");
        user.setGender(Gender.MALE);
        user.setInsurance("SDFIOJESFEF");
        user.setDateOfBirth(LocalDate.of(1970, 12, 1));
        user.setPhoneNumber("1234567890");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        UserResponseDTO firstCall = userService.getUserDetails(userId);

        verify(userRepository, times(1)).findById(userId);
        assertNotNull(firstCall);
        assertEquals(userId, firstCall.id());
        assertEquals("Marvin", firstCall.firstName());

        String redisKey = "USER_CACHE::" + userId;

        String cachedValue = stringRedisTemplate.opsForValue().get(redisKey);
        assertNotNull(cachedValue, "Expected " + redisKey + " to exist in Redis");

        UserResponseDTO secondCall = userService.getUserDetails(userId);

        assertNotNull(secondCall);
        assertEquals(userId, secondCall.id());
        assertEquals("Marvin", secondCall.firstName());
        assertEquals("Branagh", secondCall.lastName());
        assertEquals(Gender.MALE.name(), secondCall.gender());
        assertEquals("1234567890", secondCall.phoneNumber());

        verify(userRepository, times(1)).findById(userId);
    }
}