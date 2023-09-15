package com.example.librarymanager.Commons;

import com.example.librarymanager.DTOs.ApiResponse;

public class ResponseApi {
    public static ApiResponse response(Object data, String message) {
        ApiResponse response = new ApiResponse();
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
