package com.example.librarymanager;

import com.example.librarymanager.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;

@SpringBootApplication
@EnableScheduling
public class LibraryManagerApplication
//        implements CommandLineRunner
{
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    public static void main(String[] args) throws IOException {
        SpringApplication.run(LibraryManagerApplication.class, args);
    }

    @Scheduled(cron = "0 * * * * ?")
    public void cronJobSch() throws Exception {
    }

}

