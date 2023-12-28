package com.example.librarymanager.Services;

import com.example.librarymanager.DTOs.*;
import com.example.librarymanager.Entity.ContactEntity;
import com.example.librarymanager.Entity.UserContactEntity;
import com.example.librarymanager.Entity.UserEntity;
import com.example.librarymanager.Entity.UserScheduleEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {
    public Profile login(Login login) throws Exception;

    public String register(Register register) throws Exception;
    public String changePassword(ChangePass changePass) throws Exception;

    public String confirm(Long activeCode, String email, String fcm) throws Exception;

    public String changeDevice(String fcm, Long uid) throws Exception;

    public Profile getProfile(Long uid) throws Exception;

    public UserEntity editProfile(Profile profile, MultipartFile file) throws Exception;

    public List<UserScheduleEntity> getSchedules(Long uid) throws Exception;

    public String addSchedule(UserScheduleEntity schedule) throws Exception;

    public String updateSchedule(UserScheduleEntity schedule) throws Exception;

    public String deleteSchedule(Long id) throws Exception;

    public List<UserScheduleEntity> getScheduleByUid(Long uid) throws Exception;

    public String sendContact(ContactEntity contact) throws Exception;
    public String changeContactStatus(ContactStatus contactStatus) throws Exception;

    public String deleteContact(Long id) throws Exception;

    public String createContact(ContactRequest contact) throws Exception;

    public List<ContactEntity> getContactDetail(Long enquiryId) throws Exception;

    public List<UserContactEntity> getContacts() throws Exception;

    public List<UserContactEntity> getUserContacts(Long uid) throws Exception;

    public List<UserEntity> getUsers() throws Exception;

    String changeStatus(Profile user) throws Exception;

}
