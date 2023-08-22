package com.example.librarymanager.Commons;

import com.example.librarymanager.DTOs.ApiReponse;

public class ApiResponse {
    public static ApiReponse response(Object data, String message) {
        ApiReponse response = new ApiReponse();
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
