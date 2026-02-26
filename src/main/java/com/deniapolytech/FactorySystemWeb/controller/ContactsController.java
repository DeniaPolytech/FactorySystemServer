package com.deniapolytech.FactorySystemWeb.controller;

import com.deniapolytech.FactorySystemWeb.config.JwtTokenProvider;
import com.deniapolytech.FactorySystemWeb.dto.ContactDTO;
import com.deniapolytech.FactorySystemWeb.dto.auth.RegistrationResponse;
import com.deniapolytech.FactorySystemWeb.dto.contacts.*;
import com.deniapolytech.FactorySystemWeb.model.entity.Contact;
import com.deniapolytech.FactorySystemWeb.model.entity.User;
import com.deniapolytech.FactorySystemWeb.repository.UserRepository;
import com.deniapolytech.FactorySystemWeb.service.ContactService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("client/server/contacts")
public class ContactsController {
    private final ContactService contactService;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    ContactsController(ContactService contactService,
                       UserRepository userRepository,
                       JwtTokenProvider jwtTokenProvider) {
        this.contactService = contactService;
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }


    private ResponseEntity<?> checkUserAuthorization(HttpServletRequest request, int userId) {
        try {
            String token = getTokenFromRequest(request);
            if (token == null || !jwtTokenProvider.validateToken(token)) {
                return ResponseEntity.status(401)
                        .body(new MakeContactsResponse(false, "Требуется авторизация"));
            }

            String username = jwtTokenProvider.getUsernameFromToken(token);
            User user = userRepository.findByUsername(username);

            if (user == null) {
                return ResponseEntity.status(401)
                        .body(new MakeContactsResponse(false, "Пользователь не найден"));
            }


            if (user.getId() != userId) {
                return ResponseEntity.status(403)
                        .body(new MakeContactsResponse(false, "Доступ запрещен"));
            }

            return null;
        } catch (Exception e) {
            return ResponseEntity.status(401)
                    .body(new MakeContactsResponse(false, "Ошибка авторизации: " + e.getMessage()));
        }
    }

    @GetMapping("/users/{username}")
    @PreAuthorize("hasAnyRole('CLIENT', 'ADMIN')")
    @Cacheable(value = "contacts", key = "#username")
    public ResponseEntity<AllContactsResponse> getContactsByUsername(
            @PathVariable String username,
            HttpServletRequest request) {
        try {
            // Проверяем авторизацию
            String token = getTokenFromRequest(request);
            if (token == null || !jwtTokenProvider.validateToken(token)) {
                return ResponseEntity.status(401)
                        .body(new AllContactsResponse(false, "Требуется авторизация", null));
            }

            // Получаем текущего пользователя из токена
            String currentUsername = jwtTokenProvider.getUsernameFromToken(token);
            User currentUser = userRepository.findByUsername(currentUsername);

            // Проверяем, что пользователь запрашивает свои контакты
            if (!currentUsername.equals(username) && !"admin".equalsIgnoreCase(currentUser.getRole())) {
                return ResponseEntity.status(403)
                        .body(new AllContactsResponse(false, "Доступ запрещен", null));
            }

            User user = userRepository.findByUsername(username);
            if (user == null) {
                return ResponseEntity.ok(new AllContactsResponse(true, "Пользователь не найден", null));
            }

            List<Contact> contacts = contactService.getAllContactsById(user.getId());
            List<ContactDTO> contactDTOs = contacts.stream()
                    .map(ContactDTO::fromEntity)
                    .toList();
            return ResponseEntity.ok(new AllContactsResponse(true, "Контакты получены", contactDTOs));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new AllContactsResponse(false, "Ошибка получения контактов: " + e.getMessage(), null));
        }
    }

    @GetMapping("/users/{firstUserId}/contacts/{secondUserId}")
    @PreAuthorize("hasAnyRole('CLIENT', 'ADMIN')")
    public ResponseEntity<TwoContactsResponse> checkContact(
            @PathVariable int firstUserId,
            @PathVariable int secondUserId,
            HttpServletRequest request) {
        try {
            // Проверяем авторизацию первого пользователя
            ResponseEntity<?> authResponse = checkUserAuthorization(request, firstUserId);
            if (authResponse != null) {
                return (ResponseEntity<TwoContactsResponse>) authResponse;
            }

            boolean isContact = contactService.checkContacts(firstUserId, secondUserId);
            return ResponseEntity.ok(new TwoContactsResponse(true, "Проверка успешно выполнена", isContact));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new TwoContactsResponse(false, "Ошибка проверки: " + e.getMessage()));
        }
    }

    @PostMapping("/makeContact")
    @PreAuthorize("hasAnyRole('CLIENT', 'ADMIN')")
    @CacheEvict(value = "contacts", allEntries = true)
    public ResponseEntity<MakeContactsResponse> makeContact(
            @RequestBody MakeContactsRequest request,
            HttpServletRequest httpRequest) {
        try {

            ResponseEntity<?> authResponse = checkUserAuthorization(httpRequest, request.getFirstUserId());
            if (authResponse != null) {
                return (ResponseEntity<MakeContactsResponse>) authResponse;
            }

            contactService.makeContact(request.getFirstUserId(), request.getSecondUserId());
            return ResponseEntity.ok()
                    .body(new MakeContactsResponse(true, "Контакт успешно создан"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new MakeContactsResponse(false, "Ошибка при создании контакта: " + e.getMessage()));
        }
    }


    @PostMapping
    @PreAuthorize("hasAnyRole('CLIENT', 'ADMIN')")
    @CacheEvict(value = "contacts", allEntries = true)
    //@CacheEvict(value = "contacts", key = "#request.firstUserId")
    public ResponseEntity<MakeContactsResponse> createContact(
            @RequestBody MakeContactsRequest request,
            HttpServletRequest httpRequest) {
        return makeContact(request, httpRequest);
    }

    @DeleteMapping("/users/{firstUserId}/contacts/{secondUserId}")
    @PreAuthorize("hasAnyRole('CLIENT', 'ADMIN')")
    @CacheEvict(value = "contacts", allEntries = true)
    //@CacheEvict(value = "contacts", key = "#firstUserId")
    public ResponseEntity<DeleteContactResponse> deleteContact(
            @PathVariable int firstUserId,
            @PathVariable int secondUserId,
            HttpServletRequest request) {
        try {
            // Проверяем авторизацию первого пользователя
            ResponseEntity<?> authResponse = checkUserAuthorization(request, firstUserId);
            if (authResponse != null) {
                return (ResponseEntity<DeleteContactResponse>) authResponse;
            }

            contactService.deleteContactBetweenUsers(firstUserId, secondUserId);
            return ResponseEntity.ok()
                    .body(new DeleteContactResponse(true, "Контакт успешно удален"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new DeleteContactResponse(false, "Ошибка удаления: " + e.getMessage()));
        }
    }
}