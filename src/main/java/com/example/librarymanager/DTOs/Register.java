package com.example.librarymanager.DTOs;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class Register {
    private String username;
    private String password;
    private String email;
    private String fcm;
}
