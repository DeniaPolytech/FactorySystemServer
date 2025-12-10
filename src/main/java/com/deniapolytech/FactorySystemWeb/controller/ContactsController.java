package com.deniapolytech.FactorySystemWeb.controller;

import com.deniapolytech.FactorySystemWeb.dto.auth.RegistrationResponse;
import com.deniapolytech.FactorySystemWeb.dto.contacts.*;
import com.deniapolytech.FactorySystemWeb.model.entity.Contact;
import com.deniapolytech.FactorySystemWeb.model.entity.User;
import com.deniapolytech.FactorySystemWeb.repository.UserRepository;
import com.deniapolytech.FactorySystemWeb.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("client/server/contacts")
public class ContactsController {
    private final ContactService contactService;
    private final UserRepository userRepository;

    @Autowired
    ContactsController(ContactService contactService, UserRepository userRepository){
        this.contactService = contactService;
        this.userRepository = userRepository;
    }


    @GetMapping("/users/{username}")
    public ResponseEntity<AllContactsResponse> getContactsByUsername(
            @PathVariable String username) {
        try {
            User user = userRepository.findByUsername(username);
            if (user == null) {
                return ResponseEntity.ok(new AllContactsResponse(true, "Пользователь не найден", null));
            }

            List<Contact> contacts = contactService.getAllContactsById(user.getId());
            return ResponseEntity.ok(new AllContactsResponse(true, "Контакты получены", contacts));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new AllContactsResponse(false, "Ошибка получения контактов: " + e.getMessage()));
        }
    }


    @GetMapping("/users/{firstUserId}/contacts/{secondUserId}")
    public ResponseEntity<TwoContactsResponse> checkContact(
            @PathVariable int firstUserId,
            @PathVariable int secondUserId) {
        try {
            boolean isContact = contactService.checkContacts(firstUserId, secondUserId);
            return ResponseEntity.ok(new TwoContactsResponse(true, "Проверка успешно выполнена", isContact));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new TwoContactsResponse(false, "Ошибка проверки: " + e.getMessage()));
        }
    }

    @PostMapping("/makeContact")
    public ResponseEntity<MakeContactsResponse> makeContact(
            @RequestBody MakeContactsRequest request
    ){
        try{
            contactService.makeContact(request.getFirstUserId(), request.getSecondUserId());
            return ResponseEntity.ok()
                    .body(new MakeContactsResponse(true, "Контакт успешно создан"));
        }catch(Exception e){
            return ResponseEntity.internalServerError()
                    .body(new MakeContactsResponse(false, "Ошибка при регистрации: " + e.getMessage()));
        }
    }

    @DeleteMapping("/users/{firstUserId}/contacts/{secondUserId}")
    public ResponseEntity<DeleteContactResponse> deleteContact(
            @PathVariable int firstUserId,
            @PathVariable int secondUserId) {
        try {
            contactService.deleteContactBetweenUsers(firstUserId, secondUserId);
            return ResponseEntity.ok()
                    .body(new DeleteContactResponse(true, "Контакт успешно удален"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new DeleteContactResponse(false, "Ошибка удаления: " + e.getMessage()));
        }
    }

}
