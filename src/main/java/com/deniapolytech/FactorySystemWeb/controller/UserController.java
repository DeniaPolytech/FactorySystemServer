package com.deniapolytech.FactorySystemWeb.controller;

import com.deniapolytech.FactorySystemWeb.config.JwtTokenProvider;
import com.deniapolytech.FactorySystemWeb.dto.LoginRequest;
import com.deniapolytech.FactorySystemWeb.dto.LoginResponse;
import com.deniapolytech.FactorySystemWeb.dto.RegistrationRequest;
import com.deniapolytech.FactorySystemWeb.dto.RegistrationResponse;
import com.deniapolytech.FactorySystemWeb.model.User;
import com.deniapolytech.FactorySystemWeb.repository.UserRepository;
import com.deniapolytech.FactorySystemWeb.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/server/users")
public class UserController {
    private final UserService userService;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public UserController(UserService userService, UserRepository userRepository, JwtTokenProvider jwtTokenProvider) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/register")
    public ResponseEntity<RegistrationResponse> register(@RequestBody RegistrationRequest request) {

        if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new RegistrationResponse(false, "Email обязателен"));
        }

        if (request.getUsername() == null || request.getUsername().trim().isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new RegistrationResponse(false, "Имя пользователя обязательно"));
        }

        if (request.getPassword() == null || request.getPassword().isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new RegistrationResponse(false, "Пароль обязателен"));
        }

        User existingUser = userRepository.findByUsername(request.getUsername().trim());
        if (existingUser != null) {
            return ResponseEntity.badRequest()
                    .body(new RegistrationResponse(false, "Пользователь с таким именем уже существует"));
        }

        User existingEmail = userRepository.findByEmail(request.getEmail().trim());
        if (existingEmail != null) {
            return ResponseEntity.badRequest()
                    .body(new RegistrationResponse(false, "Пользователь с таким email уже существует"));
        }

        try {
            String hashedPassword = passwordEncoder.encode(request.getPassword());
            User newUser = new User(request.getUsername(), request.getEmail(), hashedPassword);

            userRepository.save(newUser);

            // Генерируем токен после успешной регистрации
            String token = jwtTokenProvider.generateToken(newUser.getUsername());

            return ResponseEntity.ok()
                    .body(new RegistrationResponse(true, "Пользователь успешно зарегистрирован", token, newUser.getUsername()));

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new RegistrationResponse(false, "Ошибка при регистрации: " + e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {

        if (request.getUsername() == null || request.getUsername().trim().isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new LoginResponse(false, "Имя пользователя обязательно"));
        }

        if (request.getPassword() == null || request.getPassword().isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new LoginResponse(false, "Пароль обязателен"));
        }

        User existingUser = userRepository.findByUsername(request.getUsername().trim());

        if (existingUser == null) {
            return ResponseEntity.badRequest()
                    .body(new LoginResponse(false, "Пользователь не найден"));
        }

        if(!passwordEncoder.matches(request.getPassword(), existingUser.getPasswordHash())){
            return ResponseEntity.badRequest()
                    .body(new LoginResponse(false, "Неверный пароль"));
        }

        try {
            // Генерируем токен после успешного входа
            String token = jwtTokenProvider.generateToken(existingUser.getUsername());

            return ResponseEntity.ok()
                    .body(new LoginResponse(true, "Вход успешно выполнен", token, existingUser.getUsername()));

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new LoginResponse(false, "Ошибка входа: " + e.getMessage()));
        }
    }
}
