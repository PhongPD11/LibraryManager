package com.example.librarymanager.Services.Implement;

import com.example.librarymanager.DTOs.PnsRequest;
import com.example.librarymanager.DTOs.UserNotify;
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
import java.util.Optional;

import static com.example.librarymanager.Commons.Commons.*;

@Service
public class FcmServiceImpl implements FcmService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserNotificationRepository userNotificationRepository;

    public String sendNotifyToAll(PnsRequest pnsRequest) throws Exception {
        List<UserEntity> users = userRepository.findAll();
        if (!users.isEmpty()) {
            Long id = -1L;
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
                }

                LocalDateTime currentTime = LocalDateTime.now();
                UserNotificationEntity saveNotify = new UserNotificationEntity();
                saveNotify.setContent(pnsRequest.getMessage());
                saveNotify.setTitle(pnsRequest.getTitle());
                saveNotify.setType(pnsRequest.getType());
                saveNotify.setIsRead(false);
                saveNotify.setCreateAt(currentTime);
                saveNotify.setUid(user.getUid());
                saveNotify.setTo("Tất cả");

                saveNotify = userNotificationRepository.save(saveNotify);
                if (id < 0L) {
                    id =  saveNotify.getId();
                }
                saveNotify.setNotifyId(id);
                userNotificationRepository.save(saveNotify);
            }

        } else throw new Exception("Users dont exist");
        return SUCCESS;
    }

    @Override
    public StringBuilder sendNotifyToUser(UserNotify pnsRequest) throws Exception {
        if (pnsRequest.getListUid() != null) {
            Long nid = -1L;
            StringBuilder sendFail = new StringBuilder();
            for (Long uid : pnsRequest.getListUid()) {
                Optional<UserEntity> user = Optional.ofNullable(userRepository.findByUid(uid));
                if (user.isPresent()) {
                    if (StringUtils.isNotBlank(user.get().getFcm())) {
                        Message message = Message.builder()
                                .putData("title", pnsRequest.getTitle())
                                .putData("message", pnsRequest.getMessage())
                                .setToken(user.get().getFcm())
                                .build();
                        try {
                            FirebaseMessaging.getInstance().send(message);
                        } catch (FirebaseMessagingException ignored) {
                            System.out.println("Send FCM to User " + user.get().getUid() + " failure!");
                        }
                        LocalDateTime currentTime = LocalDateTime.now();
                        UserNotificationEntity saveNotify = new UserNotificationEntity();
                        saveNotify.setUid(user.get().getUid());
                        saveNotify.setContent(pnsRequest.getMessage());
                        saveNotify.setTitle(pnsRequest.getTitle());
                        saveNotify.setType(pnsRequest.getType());
                        saveNotify.setIsRead(false);
                        saveNotify.setCreateAt(currentTime);
                        saveNotify.setTo("Tuỳ chọn");
                        saveNotify = userNotificationRepository.save(saveNotify);
                        if (nid < 0) {
                            nid = saveNotify.getId();
                        }
                        saveNotify.setNotifyId(nid);
                        userNotificationRepository.save(saveNotify);
                    }
                } else sendFail.append(uid.toString()).append(" ");
            } return sendFail;
        } else throw new Exception(DATA_NULL);
    }

    @Override
    public String sendWarningToUser(Long uid, String title, String content, String type) throws Exception {
        if (uid != null) {
                Optional<UserEntity> user = Optional.ofNullable(userRepository.findByUid(uid));
                if (user.isPresent()) {
                    if (StringUtils.isNotBlank(user.get().getFcm())) {
                        Message message = Message.builder()
                                .putData("title", title)
                                .putData("message", content)
                                .setToken(user.get().getFcm())
                                .build();
                        try {
                            FirebaseMessaging.getInstance().send(message);
                        } catch (FirebaseMessagingException ignored) {
                            System.out.println("Send FCM to User " + user.get().getUid() + " failure!");
                        }
                        LocalDateTime currentTime = LocalDateTime.now();
                        UserNotificationEntity saveNotify = new UserNotificationEntity();
                        saveNotify.setUid(user.get().getUid());
                        saveNotify.setContent(content);
                        saveNotify.setTitle(title);
                        saveNotify.setType(type);
                        saveNotify.setIsRead(false);
                        saveNotify.setCreateAt(currentTime);
                        saveNotify.setTo("Tuỳ chọn");
                        saveNotify = userNotificationRepository.save(saveNotify);
                        saveNotify.setNotifyId(saveNotify.getId());
                        userNotificationRepository.save(saveNotify);
                        return SUCCESS;
                    } else throw new Exception(DATA_NULL);
                } else throw new Exception(USER_NOT_FOUND);
        } else throw new Exception(DATA_NULL);
    }

    @Override
    public String userRead(Long id) throws Exception {
        if (id != null) {
            Optional<UserNotificationEntity> notify = userNotificationRepository.findById(id);
            if (notify.isPresent()){
                notify.get().setIsRead(true);
                userNotificationRepository.save(notify.get());
                return SUCCESS;
            } else throw new Exception(NOT_EXIST);
        } else throw new Exception(DATA_NULL);
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

    @Override
    public String sendFcmToken(Long uid, String fcm) throws Exception {
        if (uid != null && StringUtils.isNotBlank(fcm)){
            UserEntity user = userRepository.findByUid(uid);
            if (user != null){
                user.setFcm(fcm);
                userRepository.save(user);
                return SUCCESS;
            } else throw new Exception(USER_NOT_FOUND);
        } else throw new Exception(DATA_NULL);
    }

    @Override
    public List<UserNotificationEntity> getNotifications() throws Exception {
        return userNotificationRepository.findAll();
    }
}
