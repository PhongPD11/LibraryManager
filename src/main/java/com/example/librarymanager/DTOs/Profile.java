package com.example.librarymanager.DTOs;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Profile {
    private Long uid;
    private String fullName;
    private String email;
    private Long classId;
    private String major;
    private String token;
}
