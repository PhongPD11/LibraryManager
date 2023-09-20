package com.example.librarymanager.DTOs;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PnsRequest {
    private String fcmToken;
    private String message;
    private String title;
}
