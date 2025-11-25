package com.deniapolytech.FactorySystemWeb.service;

import com.deniapolytech.FactorySystemWeb.model.entity.Contact;
import com.deniapolytech.FactorySystemWeb.repository.ContactsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContactService {
    @Autowired
    private ContactsRepository contactsRepository;

    public List<Contact> getAllContactsById(int userId){
        return contactsRepository.findAllContactsByUserId(userId);
    }

    public Boolean checkContacts(int firstUserId, int secondUserId){
        return contactsRepository.existsContactBetweenUsers(firstUserId, secondUserId);
    }
}
