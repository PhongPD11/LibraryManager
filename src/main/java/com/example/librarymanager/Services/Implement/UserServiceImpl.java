package com.example.librarymanager.Services.Implement;

import com.example.librarymanager.Commons.Commons;
import com.example.librarymanager.DTOs.Login;
import com.example.librarymanager.DTOs.Profile;
import com.example.librarymanager.DTOs.Register;
import com.example.librarymanager.Entity.UserEntity;
import com.example.librarymanager.Jwt.JwtTokenProvider;
import com.example.librarymanager.Jwt.UserDetail;
import com.example.librarymanager.Repository.UserRepository;
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

import java.util.Objects;
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
    PasswordEncoder passwordEncoder;

    @Autowired
    JavaMailSender mailSender;

    @Override
    public Profile login(Login login) throws Exception {
        if (login.getUsername().isEmpty() || login.getPassword().isEmpty()){
            throw new Exception("Fill your Username & Password!");
        } else {
            UserEntity user = userRepository.findByUsername(login.getUsername());
            if (user != null){
                    if (user.getIsEnabled()) {
                        Authentication authentication = authenticationManager.authenticate(
                                new UsernamePasswordAuthenticationToken(
                                        login.getUsername(),
                                        login.getPassword()
                                )
                        );
                        if (authentication.isAuthenticated()) {
                            SecurityContextHolder.getContext().setAuthentication(authentication);
                            if (StringUtils.isNotBlank(login.getFcm())){
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
                            return profile;
                        } else throw new Exception(INVALID_PASSWORD);
                    } else throw new Exception("User is not verify.");
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
        if (StringUtils.isBlank(register.getEmail()) || StringUtils.isBlank(register.getUsername())
                || StringUtils.isBlank(register.getPassword()) || StringUtils.isBlank(register.getFullName())
                || StringUtils.isBlank(register.getMajor()) || register.getClassId() == null) {
            throw new Exception("You must fill all!");
        } else {

            if (studentPattern.matcher(register.getEmail()).matches()) {
                uid = Long.parseLong(register.getEmail().substring(0, 8));
                return Commons.saveUser(userRepository, passwordEncoder, register, uid, mailSender);
            } else if (teacherPattern.matcher(register.getEmail()).matches()) {
                return Commons.saveUser(userRepository, passwordEncoder, register, uid, mailSender);
            } else throw new Exception("Email is invalid!");
        }
    }

    @Override
    public String confirm(Long activeCode, String email, String fcm) throws Exception {
        UserEntity user = userRepository.findByEmail(email);
        if (user != null) {
            if(Objects.equals(user.getActiveCode(), activeCode)) {
                user.setIsEnabled(true);
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
        if (uid != null){
            UserEntity user = userRepository.findByUid(uid);
            if (user != null){
                Profile profile = new Profile();
                profile.setUid(user.getUid());
                profile.setMajor(user.getMajor());
                profile.setEmail(user.getEmail());
                profile.setClassId(user.getClassId());
                profile.setFullName(user.getFullName());
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
                    if (StringUtils.isNotBlank(url)){
                        user.setImageUrl(url);
                        user.setImageUrl(url);
                    }
                }
                userRepository.save(user);
                return user;
            } else throw new Exception(USER_NOT_FOUND);
        } else throw new Exception(UID_NULL);
    }
}
