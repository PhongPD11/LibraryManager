package com.example.librarymanager.Controller;

import com.example.librarymanager.Commons.ResponseCommon;
import com.example.librarymanager.DTOs.ApiResponse;
import com.example.librarymanager.DTOs.Login;
import com.example.librarymanager.DTOs.Profile;
import com.example.librarymanager.DTOs.Register;
import com.example.librarymanager.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class UserController {

//    @Autowired
//    JavaMailSender mailSender;

    @Autowired
    UserService service;

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
            return ResponseCommon.response(service.confirm(activeCode, email,fcm), "Success!");
        } catch (Exception e) {
            return ResponseCommon.response(null, e.getMessage());
        }
    }

    @GetMapping("/profile")
    public ApiResponse userConfirm(@RequestParam Long uid) {
        try {
            return ResponseCommon.response(service.getProfile(uid), "Success!");
        } catch (Exception e) {
            return ResponseCommon.response(null, e.getMessage());
        }
    }
    @PutMapping("/profile")
    public ApiResponse userConfirm(@RequestBody Profile profile) {
        try {
            return ResponseCommon.response(service.editProfile(profile), "Success!");
        } catch (Exception e) {
            return ResponseCommon.response(null, e.getMessage());
        }
    }

//    @PutMapping("/profile/edit")
//    public ApiResponse editProfile(@RequestBody )

}
