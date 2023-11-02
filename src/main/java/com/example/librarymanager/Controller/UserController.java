package com.example.librarymanager.Controller;

import com.example.librarymanager.Commons.ResponseCommon;
import com.example.librarymanager.DTOs.ApiResponse;
import com.example.librarymanager.DTOs.Login;
import com.example.librarymanager.DTOs.Profile;
import com.example.librarymanager.DTOs.Register;
import com.example.librarymanager.Entity.UserScheduleEntity;
import com.example.librarymanager.Services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
            return ResponseCommon.response(service.confirm(activeCode, email, fcm), "Success!");
        } catch (Exception e) {
            return ResponseCommon.response(null, e.getMessage());
        }
    }

    @GetMapping("/profile")
    public ApiResponse getUser(@RequestParam Long uid) {
        try {
            return ResponseCommon.response(service.getProfile(uid), "Success!");
        } catch (Exception e) {
            return ResponseCommon.response(null, e.getMessage());
        }
    }

    @PutMapping("/profile")
    public ApiResponse editUser(
            @RequestPart("model") String json,
            @RequestPart(value = "file", required = false) MultipartFile file) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Profile dataModel = mapper.readValue(json, Profile.class);
            return ResponseCommon.response(service.editProfile(dataModel, file), "Success!");
        } catch (Exception e) {
            return ResponseCommon.response(null, e.getMessage());
        }
    }

    @PutMapping("/schedule")
    public ApiResponse updateSchedule(@RequestBody UserScheduleEntity schedule) {
        try {
            return ResponseCommon.response(service.updateSchedule(schedule), "Success!");
        } catch (Exception e) {
            return ResponseCommon.response(null, e.getMessage());
        }
    }

    @PostMapping("/schedule")
    public ApiResponse addSchedule(@RequestBody UserScheduleEntity schedule) {
        try {
            return ResponseCommon.response(service.addSchedule(schedule), "Success!");
        } catch (Exception e) {
            return ResponseCommon.response(null, e.getMessage());
        }
    }

    @DeleteMapping("/schedule")
    public ApiResponse addSchedule(@RequestParam Long id) {
        try {
            return ResponseCommon.response(service.deleteSchedule(id), "Success!");
        } catch (Exception e) {
            return ResponseCommon.response(null, e.getMessage());
        }
    }


//    @PutMapping("/profile/edit")
//    public ApiResponse editProfile(@RequestBody )

}
