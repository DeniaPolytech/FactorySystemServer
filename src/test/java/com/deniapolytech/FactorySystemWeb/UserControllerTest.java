package com.deniapolytech.FactorySystemWeb;

import com.deniapolytech.FactorySystemWeb.config.JwtTokenProvider;
import com.deniapolytech.FactorySystemWeb.controller.UserController;
import com.deniapolytech.FactorySystemWeb.dto.auth.*;
import com.deniapolytech.FactorySystemWeb.dto.users.AllUsersResponse;
import com.deniapolytech.FactorySystemWeb.dto.users.IdByUsernameResponse;
import com.deniapolytech.FactorySystemWeb.model.entity.User;
import com.deniapolytech.FactorySystemWeb.repository.UserRepository;
import com.deniapolytech.FactorySystemWeb.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private UserController userController;

    private User testUser;
    private final BCryptPasswordEncoder realPasswordEncoder = new BCryptPasswordEncoder();

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1);
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");
        testUser.setPasswordHash(realPasswordEncoder.encode("password123"));
        testUser.setRole("client"); // Добавляем роль
    }

    @Test
    void getAllUsers_Success() {
        // Arrange
        List<User> users = Arrays.asList(testUser);
        when(userRepository.findAll()).thenReturn(users);

        // Act
        ResponseEntity<AllUsersResponse> response = userController.getAllUsers();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isSuccess());
        assertEquals("Информация о всех пользователях", response.getBody().getMessage());
        assertEquals(1, response.getBody().getData().size());
    }

    @Test
    void getAllUsers_Exception() {
        // Arrange
        when(userRepository.findAll()).thenThrow(new RuntimeException("Database error"));

        // Act
        ResponseEntity<AllUsersResponse> response = userController.getAllUsers();

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertFalse(response.getBody().isSuccess());
        assertTrue(response.getBody().getMessage().contains("Ошибка получения информации"));
    }

    @Test
    void getIdByUsername_UserNotFound() {
        // Arrange
        when(userRepository.findByUsername("nonexistent")).thenReturn(null);

        // Act
        ResponseEntity<IdByUsernameResponse> response = userController.getIdByUsername("nonexistent");

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void register_Success() {
        // Arrange
        RegistrationRequest request = new RegistrationRequest();
        request.setUsername("newuser");
        request.setEmail("new@example.com");
        request.setPassword("password123");

        when(userRepository.findByUsername("newuser")).thenReturn(null);
        when(userRepository.findByEmail("new@example.com")).thenReturn(null);
        when(userService.registerUser(anyString(), anyString(), anyString())).thenReturn(testUser);
        when(passwordEncoder.encode(anyString())).thenReturn("hashedPassword");
        when(jwtTokenProvider.generateToken(anyString(), anyString())).thenReturn("test-token");

        // Act
        ResponseEntity<RegistrationResponse> response = userController.register(request);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isSuccess());
        assertEquals("Пользователь успешно зарегистрирован", response.getBody().getMessage());
        assertEquals("test-token", response.getBody().getToken());
        assertEquals("client", response.getBody().getRole()); // Проверяем роль
    }

    @Test
    void register_MissingEmail() {
        // Arrange
        RegistrationRequest request = new RegistrationRequest();
        request.setUsername("testuser");
        request.setPassword("password123");
        // Email is null

        // Act
        ResponseEntity<RegistrationResponse> response = userController.register(request);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertFalse(response.getBody().isSuccess());
        assertEquals("Email обязателен", response.getBody().getMessage());
    }

    @Test
    void register_UsernameAlreadyExists() {
        // Arrange
        RegistrationRequest request = new RegistrationRequest();
        request.setUsername("existinguser");
        request.setEmail("test@example.com");
        request.setPassword("password123");

        when(userRepository.findByUsername("existinguser")).thenReturn(testUser);

        // Act
        ResponseEntity<RegistrationResponse> response = userController.register(request);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertFalse(response.getBody().isSuccess());
        assertEquals("Пользователь с таким именем уже существует", response.getBody().getMessage());
    }

    @Test
    void login_Success() {
        // Arrange
        LoginRequest request = new LoginRequest();
        request.setUsername("testuser");
        request.setPassword("password123");

        when(userRepository.findByUsername("testuser")).thenReturn(testUser);
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(jwtTokenProvider.generateToken(anyString(), anyString())).thenReturn("test-token");

        // Act
        ResponseEntity<LoginResponse> response = userController.login(request);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isSuccess());
        assertEquals("Вход успешно выполнен", response.getBody().getMessage());
        assertEquals("test-token", response.getBody().getToken());

    }

    @Test
    void login_WrongPassword() {
        // Arrange
        LoginRequest request = new LoginRequest();
        request.setUsername("testuser");
        request.setPassword("wrongpassword");

        when(userRepository.findByUsername("testuser")).thenReturn(testUser);
        when(passwordEncoder.matches("wrongpassword", testUser.getPasswordHash())).thenReturn(false);

        // Act
        ResponseEntity<LoginResponse> response = userController.login(request);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertFalse(response.getBody().isSuccess());
        assertEquals("Неверный пароль", response.getBody().getMessage());
    }

    @Test
    void login_UserNotFound() {
        // Arrange
        LoginRequest request = new LoginRequest();
        request.setUsername("nonexistent");
        request.setPassword("password123");

        when(userRepository.findByUsername("nonexistent")).thenReturn(null);

        // Act
        ResponseEntity<LoginResponse> response = userController.login(request);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertFalse(response.getBody().isSuccess());
        assertEquals("Пользователь не найден", response.getBody().getMessage());
    }

    @Test
    void validateToken_Success() {
        // Arrange
        TokenValidationRequest request = new TokenValidationRequest();
        request.setToken("valid-token");

        when(jwtTokenProvider.validateToken("valid-token")).thenReturn(true);
        when(jwtTokenProvider.getUsernameFromToken("valid-token")).thenReturn("testuser");
        when(jwtTokenProvider.getRoleFromToken("valid-token")).thenReturn("client");
        when(userRepository.findByUsername("testuser")).thenReturn(testUser);

        // Act
        ResponseEntity<TokenValidationResponse> response = userController.validateToken(request);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isSuccess());
        assertEquals("Токен валиден", response.getBody().getMessage());
        assertEquals("testuser", response.getBody().getUsername());
        assertEquals("client", response.getBody().getRole()); // Проверяем роль
    }

    @Test
    void validateToken_InvalidToken() {
        // Arrange
        TokenValidationRequest request = new TokenValidationRequest();
        request.setToken("invalid-token");

        when(jwtTokenProvider.validateToken("invalid-token")).thenReturn(false);

        // Act
        ResponseEntity<TokenValidationResponse> response = userController.validateToken(request);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertFalse(response.getBody().isSuccess());
        assertEquals("Невалидный токен", response.getBody().getMessage());
    }

    @Test
    void validateToken_TokenEmpty() {
        // Arrange
        TokenValidationRequest request = new TokenValidationRequest();
        request.setToken("");

        // Act
        ResponseEntity<TokenValidationResponse> response = userController.validateToken(request);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertFalse(response.getBody().isSuccess());
        assertEquals("Токен обязателен", response.getBody().getMessage());
    }

    @Test
    void validateToken_UserNotFound() {
        // Arrange
        TokenValidationRequest request = new TokenValidationRequest();
        request.setToken("valid-token");

        when(jwtTokenProvider.validateToken("valid-token")).thenReturn(true);
        when(jwtTokenProvider.getUsernameFromToken("valid-token")).thenReturn("nonexistent");
        when(userRepository.findByUsername("nonexistent")).thenReturn(null);

        // Act
        ResponseEntity<TokenValidationResponse> response = userController.validateToken(request);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertFalse(response.getBody().isSuccess());
        assertEquals("Пользователь не найден", response.getBody().getMessage());
    }

    @Test
    void refreshToken_Success() {
        // Arrange
        RefreshTokenRequest request = new RefreshTokenRequest();
        request.setToken("old-token");

        when(jwtTokenProvider.validateToken("old-token")).thenReturn(true);
        when(jwtTokenProvider.getUsernameFromToken("old-token")).thenReturn("testuser");
        when(userRepository.findByUsername("testuser")).thenReturn(testUser);
        when(jwtTokenProvider.generateToken("testuser", "client")).thenReturn("new-token");

        // Act
        ResponseEntity<RefreshTokenResponse> response = userController.refreshToken(request);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isSuccess());
        assertEquals("Токен обновлен", response.getBody().getMessage());
        assertEquals("new-token", response.getBody().getToken());
        assertEquals("client", response.getBody().getRole()); // Проверяем роль
    }

    @Test
    void refreshToken_EmptyToken() {
        // Arrange
        RefreshTokenRequest request = new RefreshTokenRequest();
        request.setToken("");

        // Act
        ResponseEntity<RefreshTokenResponse> response = userController.refreshToken(request);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertFalse(response.getBody().isSuccess());
        assertEquals("Токен обязателен", response.getBody().getMessage());
    }

    @Test
    void refreshToken_InvalidToken() {
        // Arrange
        RefreshTokenRequest request = new RefreshTokenRequest();
        request.setToken("invalid-token");

        when(jwtTokenProvider.validateToken("invalid-token")).thenReturn(false);

        // Act
        ResponseEntity<RefreshTokenResponse> response = userController.refreshToken(request);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertFalse(response.getBody().isSuccess());
        assertEquals("Невалидный токен", response.getBody().getMessage());
    }

    @Test
    void refreshToken_UserNotFound() {
        // Arrange
        RefreshTokenRequest request = new RefreshTokenRequest();
        request.setToken("valid-token");

        when(jwtTokenProvider.validateToken("valid-token")).thenReturn(true);
        when(jwtTokenProvider.getUsernameFromToken("valid-token")).thenReturn("nonexistent");
        when(userRepository.findByUsername("nonexistent")).thenReturn(null);

        // Act
        ResponseEntity<RefreshTokenResponse> response = userController.refreshToken(request);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertFalse(response.getBody().isSuccess());
        assertEquals("Пользователь не найден", response.getBody().getMessage());
    }

    @Test
    void getIdByUsername_Success() {
        // Arrange
        when(userRepository.findByUsername("testuser")).thenReturn(testUser);

        // Act
        ResponseEntity<IdByUsernameResponse> response = userController.getIdByUsername("testuser");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isSuccess());
        assertEquals("Id пользователя получен", response.getBody().getMessage());
        assertEquals(1, response.getBody().getUserId());
    }
}