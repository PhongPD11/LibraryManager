package com.example.librarymanager.Services;

import com.example.librarymanager.DTOs.Login;
import com.example.librarymanager.DTOs.Register;

public interface UserService {
    public String login(Login login) throws Exception;
    public String register(Register register) throws Exception;
    public String confirm(Long activeCode, String email, String fcm) throws Exception;
    public String changeDevice(String fcm, Long uid) throws Exception;
}
