package com.fladx.linkit.service;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class NotificationService {
    public void sendNotification(UUID userId, String message) {
        System.out.println(userId.toString() + ":" + message);
    }
}
