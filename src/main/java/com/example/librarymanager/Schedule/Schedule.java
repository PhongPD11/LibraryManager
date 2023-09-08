package com.example.librarymanager.Schedule;

import com.example.librarymanager.Entity.BookEntity;
import com.example.librarymanager.Entity.UserEntity;
import com.example.librarymanager.Repository.BookRepository;
import com.example.librarymanager.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class Schedule {
    @Autowired
    UserRepository userRepository;
    @Scheduled(cron = "0 * * * * ?")
    public void cronJobSch() {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        Date now = new Date();
        String strDate = sdf.format(now);
        UserEntity user = userRepository.findById(2L).get();
        System.out.println(strDate + "-- Book: " + user.getUserName());
    }

}
