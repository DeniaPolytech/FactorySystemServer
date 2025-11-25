package com.deniapolytech.FactorySystemWeb.controller;

import com.deniapolytech.FactorySystemWeb.dto.contacts.AllContactsRequest;
import com.deniapolytech.FactorySystemWeb.dto.contacts.AllContactsResponse;
import com.deniapolytech.FactorySystemWeb.dto.contacts.TwoContactsRequest;
import com.deniapolytech.FactorySystemWeb.dto.contacts.TwoContactsResponse;
import com.deniapolytech.FactorySystemWeb.model.entity.Contact;
import com.deniapolytech.FactorySystemWeb.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("server/contacts")
public class ContactsController {
    private final ContactService contactService;

    @Autowired
    ContactsController(ContactService contactService){
        this.contactService = contactService;
    }

    @PostMapping("/by-user")
    public ResponseEntity<AllContactsResponse> getContactsByUserId(
            @RequestBody AllContactsRequest request
            ){
        try {
            List<Contact> contacts = contactService.getAllContactsById(request.getUserId());
            if (contacts.isEmpty()) {
                return ResponseEntity.ok(new AllContactsResponse(true, "Контакты пользователя не найдены", contacts));
            }
            return ResponseEntity.ok(new AllContactsResponse(true, "Контакты получены", contacts));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new AllContactsResponse(false, "Ошибка получения контактов: " + e.getMessage()));
        }
    }

    @PostMapping("/isContact")
    public ResponseEntity<TwoContactsResponse> isContact(
            @RequestBody TwoContactsRequest request
            ){
        try{
            boolean isContact = contactService.checkContacts(request.getFirstUserId(), request.getSecondUserId());
            return ResponseEntity.ok(new TwoContactsResponse(true, "Проверка успешно выполнена", isContact));
        }catch (Exception e){
            return ResponseEntity.badRequest()
                    .body(new TwoContactsResponse(false, "Ошибка проверки: " + e.getMessage()));
        }
    }


}
