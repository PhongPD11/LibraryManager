package com.example.librarymanager.Services;

import com.example.librarymanager.DTOs.PnsRequest;
import com.example.librarymanager.DTOs.UserNotify;

public interface FcmService{
    String sendNotifyToAll(PnsRequest pnsRequest) throws Exception;
    String sendNotifyToUser(UserNotify pnsRequest) throws Exception;
    String userRead(Long id) throws Exception;
    Object getNotifyByUid(Long uid) throws Exception;
    String deleteNotify(Long id) throws Exception;
    String deleteAllNotify(Long uid) throws Exception;
    String sendFcmToken(Long uid, String fcm) throws Exception;
}
