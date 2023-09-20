package com.example.librarymanager.Commons;

public class ResponseCommon {
    public static com.example.librarymanager.DTOs.ApiResponse response(Object data, String message) {
        com.example.librarymanager.DTOs.ApiResponse response = new com.example.librarymanager.DTOs.ApiResponse();
        if (data != null) {
            response.setCode(200);
        } else {
            response.setCode(400);
        }
        response.setData(data);
        response.setMessage(message);
        return response;
    }
}
