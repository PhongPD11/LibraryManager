package com.example.librarymanager.Commons;

import com.example.librarymanager.DTOs.Register;
import com.example.librarymanager.Entity.UserEntity;
import com.example.librarymanager.Repository.UserRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.mail.internet.MimeMessage;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

public class Commons {
    public static final String EXIST_USERNAME = "existUsername";
    public static final String EXIST_EMAIL = "existEmail";
    public static final String EXIST = "exist";
    public static final String EXIST_LOAN = "existLoan";
    public static final String NOT_EXIST = "notExist";
    public static final String NOT_AVAILABLE = "notAvailable";
    public static final String DATA_NULL = "dataNull";
    public static final String INVALID_PASSWORD = "invalidPassword";
    public static final String USER_NOT_FOUND = "userNotFound";
    public static final String SUCCESS = "success";
    public static final String UID_NULL = "uidNull";
    public static final String SCHEDULE_BORROW = "scheduleBorrow";
    public static final String BORROWING = "borrowing";
    public static final String BORROW_RETURNED = "borrowReturned";
    public static final String BORROW_EXPIRED = "borrowExpired";
    public static final String INVALID = "invalid";
    public static final String EMPTY = "empty";
    public static Boolean isNullOrEmpty(String object) {
        if (object == null) {
            return true;
        } else return object.isEmpty() || object.trim().isEmpty();
    }

    public static Long randomActiveCode() {
        long min = 100000L;
        long max = 999999L;
        return ThreadLocalRandom.current().nextLong(min, max + 1);
    }

    public static String saveUser(UserRepository userRepository, PasswordEncoder passwordEncoder, Register register, Long uid, JavaMailSender mailSender) throws Exception {
        UserEntity existUserEmail = userRepository.findByEmail(register.getEmail());
        UserEntity existUsername = userRepository.findByUsername(register.getUsername());
        if (existUsername != null) {
            throw new Exception(EXIST_USERNAME);
        }
        if (existUserEmail != null) {
            if (existUserEmail.getIsEnabled()) {
                throw new Exception(EXIST_EMAIL);
            } else {
                existUserEmail.setPassword(passwordEncoder.encode(register.getPassword()));
                existUserEmail.setEmail(register.getEmail());
                Long code = Commons.randomActiveCode();
                existUserEmail.setActiveCode(code);
                existUserEmail.setUsername(register.getUsername());
                existUserEmail.setFullName(register.getFullName());
                existUserEmail.setMajor(register.getMajor());
                existUserEmail.setClassId(register.getClassId());
                userRepository.save(existUserEmail);
                sendActiveCode(mailSender, register.getEmail(), code, existUserEmail.getFullName());
                return SUCCESS;
            }
        }
        UserEntity saveUser = new UserEntity();
        saveUser.setIsAdmin(false);
        saveUser.setPassword(passwordEncoder.encode(register.getPassword()));
        saveUser.setEmail(register.getEmail());
        saveUser.setUsername(register.getUsername());
        saveUser.setFullName(register.getFullName());
        saveUser.setClassId(register.getClassId());
        saveUser.setMajor(register.getMajor());
        Long code = Commons.randomActiveCode();
        saveUser.setActiveCode(code);
        saveUser.setIsEnabled(false);
        if (StringUtils.isNotBlank(register.getFcm())) {
            saveUser.setFcm(register.getFcm());
        }
        if (uid != null) {
            saveUser.setUid(uid);
            userRepository.save(saveUser);
        } else {
            saveUser = userRepository.save(saveUser);
            Optional<UserEntity> newUser = userRepository.findById(saveUser.getId());
            newUser.get().setUid(10000L + newUser.get().getId());
            userRepository.save(newUser.get());
        }
        sendActiveCode(mailSender, register.getEmail(), code, saveUser.getFullName());
        return SUCCESS;
    }

    private static void sendActiveCode(JavaMailSender mailSender, String email, Long code, String fullName) throws Exception {
        String name = fullName.substring(0, fullName.indexOf(" "));
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setSubject("Verify your Smart Library account");
        helper.setFrom("smartlibrary1101@gmail.com");
        helper.setTo(email);
        String firstLine = String.format("Hey %s, you're almost ready to start enjoying Smart Library.", name);
        String secondLine = "Here is your verification code:";
        String content = String.format(
            "<div style=\"width: 600px; margin: auto; background-color: rgb(250, 253, 255);\">" +
                "<div style=\"background-color: #3abae6; height: 50px;\"></div>" +
                    "<div style=\"padding: 20px;\">" +
                    "<div style=\"text-align: center; font-size: 25px;\"><b>Email Confirmation</b></div>" +
                    "<div style=\"text-align: center;\">%s</div>" +
                    "<div style=\"text-align: center; margin-bottom: 20px;\">%s</div>" +
                    "<table align=\"center\">" +
                        "<tr><td>" +
                            "<div style=\"background-color: rgb(95, 185, 226); text-align: center; width: 120px; height: 60px; cursor: pointer;\">" +
                            "<div style=\"margin: auto; font-size: 20px; text-decoration: none; line-height: 60px; color: rgb(255, 255, 255); border: none;\"" +
                            ">%s</div></div>" +
                        "</td></tr>" +
                    "</table>" +
                "</div>" +
            "</div>", firstLine, secondLine, code);

        /*
        String firstLine = String.format("Hey %s, you're almost ready to start enjoying Smart Library.", name);
        String secondLine = "Simply click the big blue button below to verify your email address!";
        String url = String.format("http://localhost:8080/verify?activeCode=%s&email=%s", code, email);
        String content = String.format(
            "<div style=\"width: 600px; margin: auto; background-color: rgb(250, 253, 255);\">" +
                "<div style=\"background-color: #3abae6; height: 50px;\"></div>" +
                    "<div style=\"padding: 20px;\">" +
                    "<div style=\"text-align: center; font-size: 25px;\"><b>Email Confirmation</b></div>" +
                    "<div style=\"text-align: center;\">%s</div>" +
                    "<div style=\"text-align: center; margin-bottom: 20px;\">%s</div>" +
                    "<table align=\"center\">" +
                        "<tr><td>" +
                            "<div style=\"background-color: rgb(95, 185, 226); text-align: center; width: 120px; height: 60px; cursor: pointer;\">" +
                            "<a href=\"%s\" style=\"margin: auto; text-decoration: none; line-height: 60px; color: rgb(255, 255, 255); border: none;\"" +
                            ">Verify Email</a></div>" +
                        "</td></tr>" +
                    "</table>" +
                "</div>" +
            "</div>", firstLine, secondLine, url);
        */

        boolean html = true;
        helper.setText(content, html);
        mailSender.send(message);
    }
}
