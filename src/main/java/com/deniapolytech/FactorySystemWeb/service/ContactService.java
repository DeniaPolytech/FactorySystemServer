package com.deniapolytech.FactorySystemWeb.service;

import com.deniapolytech.FactorySystemWeb.model.entity.Contact;
import com.deniapolytech.FactorySystemWeb.model.entity.User;
import com.deniapolytech.FactorySystemWeb.repository.ContactsRepository;
import com.deniapolytech.FactorySystemWeb.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ContactService {
    @Autowired
    private ContactsRepository contactsRepository;
    @Autowired
    private UserRepository userRepository;

    public List<Contact> getAllContactsById(int userId){
        return contactsRepository.findAllContactsByUserId(userId);
    }

    public Boolean checkContacts(int firstUserId, int secondUserId){
        return contactsRepository.existsContactBetweenUsers(firstUserId, secondUserId);
    }

    public Contact makeContact(int firstUserId, int secondUserId){
        if(contactsRepository.existsContactBetweenUsers(firstUserId, secondUserId)){
            throw new RuntimeException("Такой контакт уже существует");
        }

        User user1 = userRepository.findById(firstUserId)
                .orElseThrow(() -> new RuntimeException("Пользователь с id " + firstUserId + " не существует"));

        User user2 = userRepository.findById(secondUserId)
                .orElseThrow(() -> new RuntimeException("Пользователь с id " + secondUserId + " не существует"));


        Contact contact = new Contact(user1, user2);

        return contactsRepository.save(contact);
    }

    @Transactional
    public void deleteContactBetweenUsers(int userId1, int userId2) {
        userRepository.findById(userId1)
                .orElseThrow(() -> new RuntimeException("Пользователь с id " + userId1 + " не существует"));

        userRepository.findById(userId2)
                .orElseThrow(() -> new RuntimeException("Пользователь с id " + userId2 + " не существует"));

        if (!contactsRepository.existsContactBetweenUsers(userId1, userId2)) {
            throw new RuntimeException("Контакт между пользователями " + userId1 + " и " + userId2 + " не существует");
        }

        contactsRepository.deleteContactBetweenUsers(userId1, userId2);
    }


    @Transactional
    public void deleteContactById(int contactId) {
        Contact contact = contactsRepository.findById(contactId)
                .orElseThrow(() -> new RuntimeException("Контакт с id " + contactId + " не существует"));

        contactsRepository.delete(contact);
    }
}
