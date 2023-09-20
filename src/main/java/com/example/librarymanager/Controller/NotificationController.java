package com.example.librarymanager.Controller;

import com.example.librarymanager.Commons.ResponseCommon;
import com.example.librarymanager.DTOs.ApiResponse;
import com.example.librarymanager.DTOs.PnsRequest;
import com.example.librarymanager.Services.FcmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NotificationController {
    @Autowired
    FcmService fcmService;
    @PostMapping("/notification")
    public ApiResponse sendSampleNotification(@RequestBody PnsRequest pnsRequest) {
        try {
            return ResponseCommon.response(fcmService.pushNotification(pnsRequest), "Success" );
        } catch (Exception e){
            return ResponseCommon.response(null, e.getMessage());
        }
    }
}
