package com.example.librarymanager.Services;

import com.example.librarymanager.DTOs.Login;
import com.example.librarymanager.DTOs.Profile;
import com.example.librarymanager.DTOs.Register;

public interface UserService {
    public Profile login(Login login) throws Exception;
    public String register(Register register) throws Exception;
    public String confirm(Long activeCode, String email, String fcm) throws Exception;
    public String changeDevice(String fcm, Long uid) throws Exception;
    public Profile getProfile(Long uid) throws Exception;
    public String editProfile(Profile profile) throws Exception;
}
