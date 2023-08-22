package com.example.librarymanager.Services;

import com.example.librarymanager.DTOs.Login;
import com.example.librarymanager.Entity.UserEntity;

public interface UserService {
    public String login(Login login) throws Exception;
}
