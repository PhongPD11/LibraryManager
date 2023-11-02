package com.example.librarymanager.DTOs;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserNotify {
    private List<Long> listUid;
    private String message;
    private String title;
    private MessageType type;
}


