package com.example.librarymanager.Controller;

import com.example.librarymanager.Commons.ResponseCommon;
import com.example.librarymanager.DTOs.ApiResponse;
import com.example.librarymanager.DTOs.PnsRequest;
import com.example.librarymanager.DTOs.UserNotify;
import com.example.librarymanager.Services.FcmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notification")
public class NotificationController {
    @Autowired
    FcmService fcmService;
    @PostMapping("/all")
    public ApiResponse sendNotifyToAll(@RequestBody PnsRequest pnsRequest) {
        try {
            return ResponseCommon.response(fcmService.sendNotifyToAll(pnsRequest), "Success" );
        } catch (Exception e){
            return ResponseCommon.response(null, e.getMessage());
        }
    }
    @PostMapping("/user")
    public ApiResponse sendNotifyToUser(@RequestBody UserNotify pnsRequest) {
        try {
            return ResponseCommon.response(fcmService.sendNotifyToUser(pnsRequest), "Success" );
        } catch (Exception e){
            return ResponseCommon.response(null, e.getMessage());
        }
    }

    @PostMapping("/read")
    public ApiResponse userRead(@RequestParam Long id) {
        try {
            return ResponseCommon.response(fcmService.userRead(id), "Success" );
        } catch (Exception e){
            return ResponseCommon.response(null, e.getMessage());
        }
    }
    @PostMapping("/fcm")
    public ApiResponse userRead(@RequestParam Long uid, String fcm) {
        try {
            return ResponseCommon.response(fcmService.sendFcmToken(uid, fcm), "Success" );
        } catch (Exception e){
            return ResponseCommon.response(null, e.getMessage());
        }
    }

    @GetMapping()
    public ApiResponse getUserNotify(@RequestParam Long uid) {
        try {
            return ResponseCommon.response(fcmService.getNotifyByUid(uid), "Success" );
        } catch (Exception e){
            return ResponseCommon.response(null, e.getMessage());
        }
    }

    @DeleteMapping("/delete")
    public ApiResponse userDeleteNotify(@RequestParam Long id) {
        try {
            return ResponseCommon.response(fcmService.deleteNotify(id), "Success" );
        } catch (Exception e){
            return ResponseCommon.response(null, e.getMessage());
        }
    }
    @DeleteMapping()
    public ApiResponse userDeleteAllNotify(@RequestParam Long uid) {
        try {
            return ResponseCommon.response(fcmService.deleteAllNotify(uid), "Success" );
        } catch (Exception e){
            return ResponseCommon.response(null, e.getMessage());
        }
    }

}
