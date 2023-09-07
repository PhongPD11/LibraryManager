package com.example.librarymanager;

import com.example.librarymanager.Entity.UserEntity;
import com.example.librarymanager.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class LibraryManagerApplication
//        implements CommandLineRunner
{
//
//    @Autowired
//    UserRepository userRepository;
//    @Autowired
//    PasswordEncoder passwordEncoder;

    public static void main(String[] args) {
        SpringApplication.run(LibraryManagerApplication.class, args);
    }

//    @Override
//    public void run(String... args) throws Exception {
//        UserEntity admin = new UserEntity();
//        UserEntity user = new UserEntity();
//
//        admin.setUserName("admin");
//        admin.setPassword(passwordEncoder.encode("phong"));
//        admin.setIsAdmin(true);
//        userRepository.save(admin);
//
//        user.setUserName("user");
//        user.setPassword(passwordEncoder.encode("phong"));
//        user.setIsAdmin(false);
//        userRepository.save(user);
//
//    }
}
