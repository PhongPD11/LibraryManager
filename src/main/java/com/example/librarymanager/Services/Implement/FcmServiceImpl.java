package com.example.librarymanager.Services.Implement;

import com.example.librarymanager.DTOs.PnsRequest;
import com.example.librarymanager.Entity.UserEntity;
import com.example.librarymanager.Entity.UserNotificationEntity;
import com.example.librarymanager.Repository.UserNotificationRepository;
import com.example.librarymanager.Repository.UserRepository;
import com.example.librarymanager.Services.FcmService;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static com.example.librarymanager.Commons.Commons.DATA_NULL;
import static com.example.librarymanager.Commons.Commons.SUCCESS;

@Service
public class FcmServiceImpl implements FcmService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserNotificationRepository userNotificationRepository;

    public String sendNotifyToAll(PnsRequest pnsRequest) throws Exception {
        List<UserEntity> users = userRepository.findAll();
        if (!users.isEmpty()) {
            for (UserEntity user : users) {
                if (StringUtils.isNotBlank(user.getFcm())) {
                    Message message = Message.builder()
                            .putData("title", pnsRequest.getTitle())
                            .putData("message", pnsRequest.getMessage())
                            .setToken(user.getFcm())
                            .build();
                    try {
                        FirebaseMessaging.getInstance().send(message);
                    } catch (FirebaseMessagingException ignored) {
                        System.out.println("Send FCM to User " + user.getUid() + " failure!");
                    }
                    LocalDateTime currentTime = LocalDateTime.now();
                    UserNotificationEntity saveNotify = new UserNotificationEntity();
                    saveNotify.setUid(user.getUid());
                    saveNotify.setContent(pnsRequest.getMessage());
                    saveNotify.setTitle(pnsRequest.getTitle());
                    saveNotify.setIsRead(false);
                    saveNotify.setCreateAt(currentTime);
                    userNotificationRepository.save(saveNotify);
                }
            }
        } else throw new Exception("Users dont exist");
        return SUCCESS;
    }

    @Override
    public String userRead(Boolean isRead) throws Exception {
        return null;
    }

    @Override
    public Object getNotifyByUid(Long uid) throws Exception {
        if (uid != null) {
            List<UserNotificationEntity> notifications = userNotificationRepository.findByUid(uid);
            if (notifications.isEmpty()) {
                throw new Exception("Notification is empty");
            } else {
                return notifications;
            }
        } else throw new Exception(DATA_NULL);
    }

    @Override
    public String deleteNotify(Long id) throws Exception {
        if (id != null) {
            userNotificationRepository.deleteById(id);
            return SUCCESS;
        } else {
            throw new Exception(DATA_NULL);
        }
    }

    @Override
    public String deleteAllNotify(Long uid) throws Exception {
        if (uid != null) {
            List<UserNotificationEntity> notifications = userNotificationRepository.findByUid(uid);
            if (notifications.isEmpty()) {
                return "Notification is empty";
            } else {
                userNotificationRepository.deleteAllInBatch(notifications);
                return SUCCESS;
            }
        } else throw new Exception(DATA_NULL);
    }
}
