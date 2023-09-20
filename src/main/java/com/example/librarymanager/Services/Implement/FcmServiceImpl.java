package com.example.librarymanager.Services.Implement;

import com.example.librarymanager.DTOs.PnsRequest;
import com.example.librarymanager.Services.FcmService;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import org.springframework.stereotype.Service;

@Service
public class FcmServiceImpl implements FcmService {
    public String pushNotification(PnsRequest pnsRequest) {
        Message message = Message.builder()
                .putData("title", pnsRequest.getTitle())
                .putData("message", pnsRequest.getMessage())
                .setToken(pnsRequest.getFcmToken())
                .build();

        String response = null;
        try {
            response = FirebaseMessaging.getInstance().send(message);
        } catch (FirebaseMessagingException e) {
            throw new RuntimeException(e.getMessage());
        }
        return response;
    }
}
