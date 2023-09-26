package com.example.librarymanager.Controller;

import com.example.librarymanager.Commons.ResponseCommon;
import com.example.librarymanager.DTOs.*;
import com.example.librarymanager.Services.Implement.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class UserController {

//    @Autowired
//    JavaMailSender mailSender;

    @Autowired
    UserServiceImpl service;

    @PostMapping("/login")
    public ApiResponse authenticateUser(@RequestBody Login login) {
        try {
            return ResponseCommon.response(service.login(login), "Success");
        } catch (Exception e) {
            return ResponseCommon.response(null, e.getMessage());
        }
    }

    @PostMapping("/register")
    public ApiResponse userRegistration(@RequestBody Register register) {
        try {
            return ResponseCommon.response(service.register(register), "Success");
        } catch (Exception e) {
            return ResponseCommon.response(null, e.getMessage());
        }
    }

    @PostMapping("/change-device")
    public ApiResponse userRegistration(@RequestBody String fcm, @RequestBody Long uid) {
        try {
            return ResponseCommon.response(service.changeDevice(fcm, uid), "Success");
        } catch (Exception e) {
            return ResponseCommon.response(null, e.getMessage());
        }
    }

    @GetMapping("/verify")
    public Object userConfirm(@RequestParam Long activeCode, String email, String fcm) {
        try {
            service.confirm(activeCode, email,fcm);
            return "Success";
        } catch (Exception e) {
            return ResponseCommon.response(null, e.getMessage());
        }
    }

//    @PutMapping("/profile/edit")
//    public ApiResponse editProfile(@RequestBody )

}
