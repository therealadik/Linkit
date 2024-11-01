package com.fladx.linkit.service;

import com.fladx.linkit.model.User;
import com.fladx.linkit.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void save(User user) {
        try{
            userRepository.save(user);
        }
        catch(Exception e){
            throw new RuntimeException("Ошибка сохранения пользователя: %s".formatted(e.getMessage()));
        }
    }

    public void register(User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Пользователь с именем: %s уже существует".formatted(user.getUsername()));
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }
}
