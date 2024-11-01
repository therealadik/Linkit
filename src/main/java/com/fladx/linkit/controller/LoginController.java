package com.fladx.linkit.controller;

import com.fladx.linkit.model.User;
import com.fladx.linkit.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {

    private final UserService userService;

    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/auth")
    public String auth() {
        return "auth.html"; // название HTML-шаблона для страницы логина
    }


    @PostMapping("/register")
    public String register(@ModelAttribute User user) {
        userService.register(user);

        return "auth.html";
    }
}
