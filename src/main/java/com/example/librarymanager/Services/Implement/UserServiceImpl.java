package com.example.librarymanager.Services.Implement;

import com.example.librarymanager.Commons.Commons;
import com.example.librarymanager.DTOs.ActiveCode;
import com.example.librarymanager.DTOs.Fcm;
import com.example.librarymanager.DTOs.Login;
import com.example.librarymanager.DTOs.Register;
import com.example.librarymanager.Entity.UserEntity;
import com.example.librarymanager.Jwt.JwtTokenProvider;
import com.example.librarymanager.Jwt.UserDetail;
import com.example.librarymanager.Repository.UserRepository;
import com.example.librarymanager.Services.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.regex.Pattern;

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

    @Override
    public String login(Login login) throws Exception {
        if (login.getUsername().isEmpty() || login.getPassword().isEmpty()){
            throw new Exception("Fill your Username & Password!");
        } else {
            UserEntity user = userRepository.findByUsername(login.getUsername());
            if (user != null){
                try {
                    Authentication authentication = authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    login.getUsername(),
                                    login.getPassword()
                            )
                    );
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    return tokenProvider.generateToken(((UserDetail) authentication.getPrincipal()).getUser());
                } catch (Exception e){
                    throw new Exception("Wrong password!");
                }
            } else throw new Exception("User not found!");
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
                || StringUtils.isBlank(register.getPassword())) {
            throw new Exception("You must fill all!");
        } else {

            if (studentPattern.matcher(register.getEmail()).matches()) {
                uid = Long.parseLong(register.getEmail().substring(0, 8));
                return Commons.saveUser(userRepository, passwordEncoder, register, uid);
            } else if (teacherPattern.matcher(register.getEmail()).matches()) {
                return Commons.saveUser(userRepository, passwordEncoder, register, uid);
            } else throw new Exception("Email is invalid!");
        }
    }

    @Override
    public String confirm(ActiveCode activeCode) throws Exception {
        UserEntity user = userRepository.findByEmail(activeCode.getEmail());
        if (user != null) {
            if(Objects.equals(user.getActiveCode(), activeCode.getActiveCode())) {
                user.setIsEnabled(true);
                userRepository.save(user);
                return "Register successfully!";
            } else throw new Exception("Active code is not correct!");
        } else throw new Exception("User does exist!");
    }

    @Override
    public String changeDevice(Fcm fcm) throws Exception {
        if (fcm.getUid() != null) {
            UserEntity existUser = userRepository.findByUid(fcm.getUid());
            if (existUser != null && StringUtils.isNotBlank(fcm.getFcm())) {
                existUser.setFcm(fcm.getFcm());
                return "Success!";
            } else throw new Exception("FCM is empty or User doesn't exist");
        } else throw new Exception("UID is null!");
    }

}
