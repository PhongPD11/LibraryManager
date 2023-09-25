package com.example.librarymanager.Services;

import com.example.librarymanager.DTOs.ActiveCode;
import com.example.librarymanager.DTOs.Fcm;
import com.example.librarymanager.DTOs.Login;
import com.example.librarymanager.DTOs.Register;

public interface UserService {
    public String login(Login login) throws Exception;
    public String register(Register register) throws Exception;
    public String confirm(ActiveCode activeCode) throws Exception;
    public String changeDevice(Fcm fcm) throws Exception;
}
