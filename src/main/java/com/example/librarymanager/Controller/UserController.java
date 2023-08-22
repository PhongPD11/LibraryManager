package com.example.librarymanager.Controller;

import com.example.librarymanager.Commons.ApiResponse;
import com.example.librarymanager.DTOs.ApiReponse;
import com.example.librarymanager.DTOs.Login;
import com.example.librarymanager.Jwt.JwtTokenProvider;
import com.example.librarymanager.Jwt.UserDetail;
import com.example.librarymanager.Services.Implement.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    public ApiReponse authenticateUser(@RequestBody Login login) {
        try {
            return ApiResponse.response(service.login(login), "Success");
        } catch (Exception e){
            return ApiResponse.response(null, e.getMessage());
        }
    }
}
