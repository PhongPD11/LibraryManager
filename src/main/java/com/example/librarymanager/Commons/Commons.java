package com.example.librarymanager.Commons;

import com.example.librarymanager.DTOs.Register;
import com.example.librarymanager.Entity.UserEntity;
import com.example.librarymanager.Repository.UserRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

public class Commons {
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

    public static String saveUser(UserRepository userRepository, PasswordEncoder passwordEncoder, Register register, Long uid) throws Exception{
        UserEntity existUserEmail = userRepository.findByEmail(register.getEmail());
        UserEntity existUsername = userRepository.findByUsername(register.getUsername());
        if (existUsername != null) {
            throw new Exception("User exist!");
        }
        if (existUserEmail != null) {
            if (existUserEmail.getIsEnabled()) {
                throw new Exception("User exist!");
            } else {
                existUserEmail.setPassword(passwordEncoder.encode(register.getPassword()));
                existUserEmail.setEmail(register.getEmail());
                Long code = Commons.randomActiveCode();
                existUserEmail.setActiveCode(code);
                existUserEmail.setUsername(register.getUsername());
                userRepository.save(existUserEmail);
//                sendActiveCode(mailSender, register.getEmail(),code);
                return "Success!";
            }
        }
        UserEntity saveUser = new UserEntity();
        saveUser.setIsAdmin(false);
        saveUser.setPassword(passwordEncoder.encode(register.getPassword()));
        saveUser.setEmail(register.getEmail());
        saveUser.setUsername(register.getUsername());
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
//        sendActiveCode(mailSender, register.getEmail(),code);
        return "Success!";
    }

//    private static void sendActiveCode(JavaMailSender mailSender, String email, Long code) throws MessagingException {
//        MimeMessage message = mailSender.createMimeMessage();
//        MimeMessageHelper helper = new MimeMessageHelper(message);
//
//        helper.setSubject("This is an HTML email");
//        helper.setFrom("smartlibrary1101@gmail.com");
//        helper.setTo(email);
//        String content = String.format("<b>Hello!</b>,<br><i>Your active code is %s</i>", code);
//        boolean html = true;
//        helper.setText(content, html);
//        mailSender.send(message);
//    }

}
