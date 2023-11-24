package com.example.librarymanager.DTOs;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AdminNotify {
    private String message;
    private String title;
    private String type;
    private String to;
    private Long notifyId;
    private List<Long> user;
}


