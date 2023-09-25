package com.example.librarymanager.Controller;

import com.example.librarymanager.Commons.ResponseCommon;
import com.example.librarymanager.DTOs.*;
import com.example.librarymanager.Services.Implement.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@RestController
@RequestMapping
public class UserController {

    @Autowired
    JavaMailSender mailSender;

    @Autowired
    UserServiceImpl service;

    @PostMapping("/login")
    public ApiResponse authenticateUser(@RequestBody Login login) {
        try {
            return ResponseCommon.response(service.login(login), "Success");
        } catch (Exception e) {
            return ResponseCommon.response(null, e.getMessage());
        }
    }

    @PostMapping("/register")
    public ApiResponse userRegistration(@RequestBody Register register) {
        try {
            return ResponseCommon.response(service.register(register), "Success");
        } catch (Exception e) {
            return ResponseCommon.response(null, e.getMessage());
        }
    }

    @PostMapping("/change-device")
    public ApiResponse userRegistration(@RequestBody Fcm fcm) {
        try {
            return ResponseCommon.response(service.changeDevice(fcm), "Success");
        } catch (Exception e) {
            return ResponseCommon.response(null, e.getMessage());
        }
    }

    @PostMapping("/verify")
    public ApiResponse userConfirm(@RequestBody ActiveCode activeCode) {
        try {
            return ResponseCommon.response(service.confirm(activeCode), "Success");
        } catch (Exception e) {
            return ResponseCommon.response(null, e.getMessage());
        }
    }
    @PostMapping("/email")
    public String userConfirm(@RequestBody String content) throws MessagingException {
        String from = "smartlibrary1101@gmail.com";
        String to = "phongpd1101@gmail.com";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setSubject("This is an HTML email");
        helper.setFrom(from);
        helper.setTo(to);

        boolean html = true;
        helper.setText("<b>Hey guys</b>,<br><i>Welcome to my new home</i>", html);

        mailSender.send(message);
        return content;
    }

}
