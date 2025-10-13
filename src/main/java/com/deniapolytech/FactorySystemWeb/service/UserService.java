package com.deniapolytech.FactorySystemWeb.service;

import com.deniapolytech.FactorySystemWeb.model.User;
import com.deniapolytech.FactorySystemWeb.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;


    public User registerUser(String username, String email, String password_hash){
        if(userRepository.existsByUsername(username)){
            throw new RuntimeException("Пользователь с таким именем уже существует");
        }
        if(userRepository.existsByEmail(email)){
            throw new RuntimeException("Пользователь с таким email уже существует");
        }


        User user = new User(username, email, password_hash);

        return userRepository.save(user);
    }
}
