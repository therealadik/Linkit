package com.fladx.linkit.service;

import com.fladx.linkit.model.User;
import com.fladx.linkit.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(){
        User user = new User();

        userRepository.save(user);
        return user;
    }

    public User getUser(UUID id){
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }
}
