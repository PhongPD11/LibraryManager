package com.example.librarymanager.DTOs;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class ChangePass {
    private String username;
    private String password;
    private String newPassword;
}
