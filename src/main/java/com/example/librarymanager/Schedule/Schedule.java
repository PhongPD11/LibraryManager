package com.example.librarymanager.Schedule;

import com.example.librarymanager.Entity.UserBookEntity;
import com.example.librarymanager.Repository.UserBookRepository;
import com.example.librarymanager.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.example.librarymanager.Commons.Commons.BORROW_EXPIRED;

@Component
public class Schedule {
    @Autowired
    UserRepository userRepository;

    @Autowired
    UserBookRepository userBookRepository;
    @Scheduled(cron = "0 * * * * ?")
    public void cronJobSch() {
        List<UserBookEntity> listExpireSoon = userBookRepository.getExpireSoon();
        if (!listExpireSoon.isEmpty()){
            for (UserBookEntity userBook : listExpireSoon){
                System.out.println(userBook.getUid() + " " + userBook.getBookId());
            }
        }
    }

    @Scheduled(cron = "5 * * * * ?")
    public void cronJobSch2() {
        List<UserBookEntity> listExpireSoon = userBookRepository.getExpired();
        if (!listExpireSoon.isEmpty()){
            for (UserBookEntity userBook : listExpireSoon){
                userBook.setStatus(BORROW_EXPIRED);
                userBookRepository.save(userBook);
                System.out.println(userBook.getUid().toString() +" "+ userBook.getBookId().toString() +" "+ userBook.getStatus());
            }
        }
    }

}
