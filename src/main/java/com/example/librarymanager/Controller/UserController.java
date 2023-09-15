package com.example.librarymanager.Controller;

import com.example.librarymanager.Commons.ResponseApi;
import com.example.librarymanager.DTOs.ApiResponse;
import com.example.librarymanager.DTOs.Login;
import com.example.librarymanager.Services.Implement.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class UserController {

    @Autowired
    UserServiceImpl service;

    @PostMapping("/login")
    public ApiResponse authenticateUser(@RequestBody Login login) {
        try {
            return ResponseApi.response(service.login(login), "Success");
        } catch (Exception e) {
            return ResponseApi.response(null, e.getMessage());
        }
    }
}
