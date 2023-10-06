package com.example.librarymanager.Services;

import com.example.librarymanager.DTOs.PnsRequest;

public interface FcmService{
    String sendNotifyToAll(PnsRequest pnsRequest) throws Exception;
    String userRead(Boolean isRead) throws Exception;
    Object getNotifyByUid(Long uid) throws Exception;
    String deleteNotify(Long id) throws Exception;
    String deleteAllNotify(Long uid) throws Exception;
}
