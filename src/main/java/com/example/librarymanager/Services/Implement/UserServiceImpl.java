package com.example.librarymanager.Services.Implement;

import com.example.librarymanager.Commons.Commons;
import com.example.librarymanager.DTOs.*;
import com.example.librarymanager.Entity.ContactEntity;
import com.example.librarymanager.Entity.UserContactEntity;
import com.example.librarymanager.Entity.UserEntity;
import com.example.librarymanager.Entity.UserScheduleEntity;
import com.example.librarymanager.Jwt.JwtTokenProvider;
import com.example.librarymanager.Jwt.UserDetail;
import com.example.librarymanager.Repository.ContactRepository;
import com.example.librarymanager.Repository.UserContactRepository;
import com.example.librarymanager.Repository.UserRepository;
import com.example.librarymanager.Repository.UserScheduleRepository;
import com.example.librarymanager.Services.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;

import static com.example.librarymanager.Commons.Commons.*;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserScheduleRepository userScheduleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JavaMailSender mailSender;

    @Autowired
    private UserContactRepository userContactRepository;

    @Autowired
    private ContactRepository contactRepository;

    @Override
    public Profile login(Login login) throws Exception {
        if (login.getUsername().isEmpty() || login.getPassword().isEmpty()) {
            throw new Exception("Fill your Username & Password!");
        } else {
            UserEntity user = userRepository.findByUsername(login.getUsername());
            if (user != null) {
                if (user.getIsEnabled() || user.getIsAdmin()) {
                    Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(login.getUsername(), login.getPassword()));
                    if (authentication.isAuthenticated()) {
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                        if (StringUtils.isNotBlank(login.getFcm())) {
                            user.setFcm(login.getFcm());
                        }
                        userRepository.save(user);
                        String authenToken = tokenProvider.generateToken(((UserDetail) authentication.getPrincipal()).getUser());
                        Profile profile = new Profile();
                        profile.setFcm(login.getFcm());
                        profile.setToken(authenToken);
                        profile.setUid(user.getUid());
                        profile.setMajor(user.getMajor());
                        profile.setEmail(user.getEmail());
                        profile.setClassId(user.getClassId());
                        profile.setFullName(user.getFullName());
                        profile.setImageUrl(user.getImageUrl());
                        return profile;
                    } else throw new Exception(INVALID_PASSWORD);
                } else throw new Exception(NOT_VERIFY +",email: "+ user.getEmail());
            } else throw new Exception(USER_NOT_FOUND);
        }
    }

    @Override
    public String register(Register register) throws Exception {
        String studentRegex = "^[1-9][0-9]{7}@student.hcmute.edu.vn$";
        String teacherRegex = "^[A-Za-z0-9+_.-]+@hcmute\\.edu\\.vn$";
        Pattern studentPattern = Pattern.compile(studentRegex);
        Pattern teacherPattern = Pattern.compile(teacherRegex);
        Long uid = null;
        if (StringUtils.isBlank(register.getEmail()) || StringUtils.isBlank(register.getUsername()) || StringUtils.isBlank(register.getPassword()) || StringUtils.isBlank(register.getFullName())) {
            throw new Exception("You must fill all!");
        } else {
            if (studentPattern.matcher(register.getEmail()).matches()) {
                if (StringUtils.isNotBlank(register.getMajor()) && register.getClassId() != null) {
                    uid = Long.parseLong(register.getEmail().substring(0, 8));
                    return Commons.saveUser(userRepository, passwordEncoder, register, uid, mailSender);
                } else throw new Exception("You must fill all!");
            } else if (teacherPattern.matcher(register.getEmail()).matches()) {
                return Commons.saveUser(userRepository, passwordEncoder, register, uid, mailSender);
            } else throw new Exception("Email is invalid!");
        }
    }

    @Override
    public String changePassword(ChangePass changePass) throws Exception {
        if (StringUtils.isNotBlank(changePass.getUsername()) &&
                StringUtils.isNotBlank(changePass.getPassword()) &&
                StringUtils.isNotBlank(changePass.getNewPassword())) {
            Optional<UserEntity> user = Optional.ofNullable(userRepository.findByUsername(changePass.getUsername()));
            if (user.isPresent()) {
                if (user.get().getIsEnabled() || user.get().getIsAdmin()) {
                    Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(changePass.getUsername(), changePass.getPassword()));
                    if (authentication.isAuthenticated()) {
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                        user.get().setPassword(changePass.getPassword());
                        userRepository.save(user.get());
                        return SUCCESS;
                    } else throw new Exception("Wrong Password");
                } else throw new Exception("Account has not been activated");
            } else throw new Exception(NOT_EXIST);
        } else throw new Exception(DATA_NULL);
    }

    @Override
    public String confirm(Long activeCode, String email, String fcm) throws Exception {
        UserEntity user = userRepository.findByEmail(email);
        if (user != null) {
            if (Objects.equals(user.getActiveCode(), activeCode)) {
                user.setIsEnabled(true);
                user.setStatus("Hoạt động");
                user.setFcm(fcm);
                userRepository.save(user);
                return "Register successfully!";
            } else throw new Exception("Active code is not correct!");
        } else throw new Exception("User does exist!");
    }

    @Override
    public String changeDevice(String fcm, Long uid) throws Exception {
        if (uid != null) {
            UserEntity existUser = userRepository.findByUid(uid);
            if (existUser != null && StringUtils.isNotBlank(fcm)) {
                existUser.setFcm(fcm);
                return SUCCESS;
            } else throw new Exception("FCM is empty or User doesn't exist");
        } else throw new Exception(UID_NULL);
    }

    @Override
    public Profile getProfile(Long uid) throws Exception {
        if (uid != null) {
            UserEntity user = userRepository.findByUid(uid);
            if (user != null) {
                Profile profile = new Profile();
                profile.setUid(user.getUid());
                profile.setMajor(user.getMajor());
                profile.setFcm(user.getFcm());
                profile.setEmail(user.getEmail());
                profile.setClassId(user.getClassId());
                profile.setFullName(user.getFullName());
                profile.setStatus(user.getStatus());
                profile.setPenaltyCount(user.getPenaltyCount());
                return profile;
            } else throw new Exception(USER_NOT_FOUND);
        } else throw new Exception(USER_NOT_FOUND);
    }

    @Override
    public UserEntity editProfile(Profile profile, MultipartFile file) throws Exception {
        if (profile.getUid() != null) {
            UserEntity user = userRepository.findByUid(profile.getUid());
            if (user != null) {
                String name = profile.getFullName();
                String major = profile.getMajor();
                Long classId = profile.getClassId();
                user.setFullName(StringUtils.isNotBlank(name) ? name : user.getFullName());
                user.setMajor(StringUtils.isNotBlank(major) ? major : user.getMajor());
                user.setClassId(classId != null && classId != 0 ? classId : user.getClassId());

                if (file != null) {
                    String url = Commons.uploadImage(file, "avatar/");
                    if (StringUtils.isNotBlank(url)) {
                        user.setImageUrl(url);
                        user.setImageUrl(url);
                    }
                }
                userRepository.save(user);
                return user;
            } else throw new Exception(USER_NOT_FOUND);
        } else throw new Exception(UID_NULL);
    }

    @Override
    public List<UserScheduleEntity> getSchedules(Long uid) throws Exception {
        List<UserScheduleEntity> schedules = userScheduleRepository.findByUid(uid);
        if (!schedules.isEmpty()) {
            return schedules;
        }
        throw new Exception(NOT_EXIST);
    }

    @Override
    public String addSchedule(UserScheduleEntity schedule) throws Exception {
        if (schedule.getUid() != null && schedule.getHourTime() != null && schedule.getMinuteTime() != null && schedule.getRepeat() != null && schedule.getIsOn() != null && StringUtils.isNotBlank(schedule.getTypeRepeat())) {
            userScheduleRepository.save(schedule);
            return SUCCESS;
        } else throw new Exception(DATA_NULL);
    }

    @Override
    public String updateSchedule(UserScheduleEntity schedule) throws Exception {
        if (schedule.getUid() != null && schedule.getId() != null && schedule.getHourTime() != null && schedule.getMinuteTime() != null && schedule.getRepeat() != null && schedule.getIsOn() != null && StringUtils.isNotBlank(schedule.getTypeRepeat())) {
            Optional<UserScheduleEntity> existSchedule = userScheduleRepository.findById(schedule.getId());
            if (existSchedule.isPresent()) {
                existSchedule.get().setRepeat(schedule.getRepeat());
                existSchedule.get().setHourTime(schedule.getHourTime());
                existSchedule.get().setMinuteTime(schedule.getMinuteTime());
                existSchedule.get().setIsOn(schedule.getIsOn());
                existSchedule.get().setTypeRepeat(schedule.getTypeRepeat());
                userScheduleRepository.save(existSchedule.get());
                return SUCCESS;
            } else throw new Exception(NOT_EXIST);
        } else throw new Exception(DATA_NULL);
    }

    @Override
    public String deleteSchedule(Long id) throws Exception {
        if (id != null) {
            Optional<UserScheduleEntity> existSchedule = userScheduleRepository.findById(id);
            if (existSchedule.isPresent()) {
                userScheduleRepository.delete(existSchedule.get());
                return SUCCESS;
            } else throw new Exception(NOT_EXIST);
        } else throw new Exception(DATA_NULL);
    }

    @Override
    public List<UserScheduleEntity> getScheduleByUid(Long uid) throws Exception {
        if (uid != null) {
            List<UserScheduleEntity> schedules = userScheduleRepository.findByUid(uid);
            if (!schedules.isEmpty()) {
                return schedules;
            } else throw new Exception(NOT_EXIST);
        } else throw new Exception(DATA_NULL);
    }

    @Override
    public String sendContact(ContactEntity contact) throws Exception {
        if (contact.getEnquiryId() != null && StringUtils.isNotBlank(contact.getContent())
                && contact.getIsAdmin() != null) {
            Optional<UserContactEntity> existContact = userContactRepository.findById(contact.getEnquiryId());
            if (existContact.isPresent()) {
                ContactEntity enquiry = new ContactEntity();
                enquiry.setEnquiryId(contact.getEnquiryId());
                enquiry.setContent(contact.getContent());
                LocalDateTime currentTime = LocalDateTime.now();
                enquiry.setTime(currentTime);
                enquiry.setIsAdmin(contact.getIsAdmin());
                if (contact.getIsAdmin()) {
                    existContact.get().setStatus("Đã phản hồi");
                } else {
                    existContact.get().setStatus("Chưa phản hồi");
                }
                userContactRepository.save(existContact.get());
                contactRepository.save(enquiry);
                return SUCCESS;
            } else throw new Exception(NOT_EXIST);
        } else throw new Exception(DATA_NULL);
    }

    @Override
    public String changeContactStatus(ContactStatus contact) throws Exception {
        if (contact.getId() != null && StringUtils.isNotBlank(contact.getStatus())) {
            Optional<UserContactEntity> existContact = userContactRepository.findById(contact.getId());
            if (existContact.isPresent()) {
                existContact.get().setStatus(contact.getStatus());
                userContactRepository.save(existContact.get());
                return SUCCESS;
            } else throw new Exception(NOT_EXIST);
        } else throw new Exception(DATA_NULL);
    }

    @Override
    public String deleteContact(Long id) throws Exception {
        if (id != null) {
            Optional<UserContactEntity> existContact = userContactRepository.findById(id);
            if (existContact.isPresent()) {
                userContactRepository.delete(existContact.get());
                List<ContactEntity> listMessage = contactRepository.findByEnquiryId(id);
                if (listMessage.isEmpty()) {
                    throw new Exception(NOT_AVAILABLE);
                } else {
                    contactRepository.deleteAllInBatch(listMessage);
                    return SUCCESS;
                }
            }
            throw new Exception(NOT_EXIST);
        }
        throw new Exception(DATA_NULL);
    }

    @Override
    public String createContact(ContactRequest contact) throws Exception {
        if (contact.getUid() != null && StringUtils.isNotBlank(contact.getContent())
                && StringUtils.isNotBlank(contact.getTitle())) {
            UserContactEntity newContact = new UserContactEntity();
            ContactEntity enquiry = new ContactEntity();
            newContact.setStatus("Mới");
            newContact.setTitle(contact.getTitle());
            LocalDateTime currentTime = LocalDateTime.now();
            newContact.setCreateAt(currentTime);
            newContact.setContent(contact.getContent());
            newContact.setUid(contact.getUid());
            newContact = userContactRepository.save(newContact);
            contact.setEnquiryId(newContact.getId());
            //
            enquiry.setEnquiryId(newContact.getId());
            enquiry.setContent(contact.getContent());
            enquiry.setTime(currentTime);
            enquiry.setIsAdmin(false);
            contactRepository.save(enquiry);
            return SUCCESS;
        } else throw new Exception(DATA_NULL);
    }

    @Override
    public List<ContactEntity> getContactDetail(Long enquiryId) throws Exception {
        if (enquiryId != null) {
            List<ContactEntity> listMessage = contactRepository.findByEnquiryId(enquiryId);
            if (listMessage.isEmpty()) {
                throw new Exception(NOT_AVAILABLE);
            } else {
                return listMessage;
            }
        } else throw new Exception(DATA_NULL);
    }

    @Override
    public List<UserContactEntity> getContacts() throws Exception {
        List<UserContactEntity> listContact = userContactRepository.findAll();
        if (listContact.isEmpty())
            throw new Exception(NOT_EXIST);
        else return listContact;
    }

    @Override
    public List<UserContactEntity> getUserContacts(Long uid) throws Exception {
        if (uid != null) {
            return userContactRepository.findByUid(uid);
        } else throw new Exception(DATA_NULL);
    }

    @Override
    public List<UserEntity> getUsers() throws Exception {
        return userRepository.findAllByIsAdminAndIsEnabled(false, true);
    }

    @Override
    public String changeStatus(Profile user) throws Exception {
        if (user.getUid() != null && StringUtils.isNotBlank(user.getStatus())) {
            Optional<UserEntity> existUser = Optional.ofNullable(userRepository.findByUid(user.getUid()));
            if (existUser.isPresent()) {
                existUser.get().setStatus(user.getStatus());
                userRepository.save(existUser.get());
                return user.getStatus();
            } else throw new Exception(NOT_EXIST);
        } else throw new Exception(DATA_NULL);
    }

}
