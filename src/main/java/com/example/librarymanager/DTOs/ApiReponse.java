package com.example.librarymanager.DTOs;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiReponse {
    private int code;
    private Object data;
    private String message;
}
