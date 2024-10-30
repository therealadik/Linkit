package com.fladx.linkit.controller;

import com.fladx.linkit.model.User;
import com.fladx.linkit.service.LinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LinkController {

    @Autowired
    private LinkService linkService;

    @PostMapping("/link/create")
    public ResponseEntity<String> createLink(@RequestParam String originalUrl, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok("Link created");
    }
}
