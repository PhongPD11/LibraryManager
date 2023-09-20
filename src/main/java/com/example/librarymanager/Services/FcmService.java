package com.example.librarymanager.Services;

import com.example.librarymanager.DTOs.PnsRequest;

public interface FcmService{
    String pushNotification(PnsRequest pnsRequest);
}
