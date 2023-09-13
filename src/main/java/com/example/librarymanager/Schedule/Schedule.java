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
import java.util.List;
import java.util.Random;

@Component
public class Schedule {
    @Autowired
    UserRepository userRepository;

    @Autowired
    BookRepository bookRepository;

    @Scheduled(cron = "0 * * * * ?")
    public void cronJobSch() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        Date now = new Date();
        String strDate = sdf.format(now);
        List<BookEntity> books = bookRepository.findAll();
        Random rand = new Random();
        int random = rand.nextInt(4);
        System.out.println(strDate + "-- Book: " + books.get(random).getName());
    }

}
