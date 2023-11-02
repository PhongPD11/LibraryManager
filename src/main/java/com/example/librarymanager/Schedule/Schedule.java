package com.example.librarymanager.Schedule;

import com.example.librarymanager.Entity.BookEntity;
import com.example.librarymanager.Entity.UserBookEntity;
import com.example.librarymanager.Entity.UserEntity;
import com.example.librarymanager.Entity.UserScheduleEntity;
import com.example.librarymanager.Repository.BookRepository;
import com.example.librarymanager.Repository.UserBookRepository;
import com.example.librarymanager.Repository.UserRepository;
import com.example.librarymanager.Repository.UserScheduleRepository;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.example.librarymanager.Commons.Commons.BORROW_EXPIRED;
import static com.example.librarymanager.Commons.Commons.BORROW_REGISTER_EXPIRED;

@Component
public class Schedule {
    @Autowired
    UserRepository userRepository;

    @Autowired
    BookRepository bookRepository;

    @Autowired
    UserBookRepository userBookRepository;

    @Autowired
    UserScheduleRepository userScheduleRepository;

    @Scheduled(cron = "0 * * * * ?")
    public void cronJobUserSch() {
        try {
            LocalDateTime currentTime = LocalDateTime.now();
            Long hour = (long) currentTime.getHour();
            Long minute = (long) currentTime.getMinute();
            List<UserScheduleEntity> listUserSch = userScheduleRepository.getAlarms(hour, minute);
            if (!listUserSch.isEmpty()){
                System.out.println("Exist");
                for (UserScheduleEntity userSchedule : listUserSch){
                    Optional<UserEntity> user = Optional.ofNullable(userRepository.findByUid(userSchedule.getUid()));
                    if (user.isPresent()){
                        if (StringUtils.isNotBlank(user.get().getFcm())){
                            System.out.println("Send to User " + user.get().getUid());
                            Message message = Message.builder()
                                    .putData("title", "Báo thức")
                                    .putData("message", "Đã đến giờ đọc sách rồi bạn ơi!")
                                    .setToken(user.get().getFcm())
                                    .build();
                            try {
                                FirebaseMessaging.getInstance().send(message);
                            } catch (FirebaseMessagingException ignored) {
                                System.out.println("Send FCM to User " + user.get().getUid() + " failure!");
                            }
                        }
                    }
                }
            }
        } catch (Exception ignore){
        }
    }

    @Scheduled(cron = "0 0 * * * ?")
    public void cronJobSch() {
        List<UserBookEntity> listExpireSoon = userBookRepository.getExpireSoon();
        if (!listExpireSoon.isEmpty()){
            for (UserBookEntity userBook : listExpireSoon){
                System.out.println(userBook.getUid() + " " + userBook.getBookId());
            }
        }
    }

    @Scheduled(cron = "0 5 * * * ?")
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

    @Scheduled(cron = "0 10 * * * ?")
    public void cronJobSch3() {
        List<UserBookEntity> listExpireSoon = userBookRepository.getExpiredRegisterSoon();
        if (!listExpireSoon.isEmpty()){
            for (UserBookEntity userBook : listExpireSoon){
                System.out.println(userBook.getUid() + " " + userBook.getBookId());
            }
        }
    }

    @Scheduled(cron = "0 15 * * * ?")
    public void cronJobSch4() {
        List<UserBookEntity> listExpireSoon = userBookRepository.getExpired();
        if (!listExpireSoon.isEmpty()){
            for (UserBookEntity userBook : listExpireSoon){
                userBook.setStatus(BORROW_REGISTER_EXPIRED);
                Optional<BookEntity> book = Optional.ofNullable(bookRepository.findByBookId(userBook.getBookId()));
                if (book.isPresent()){
                    book.get().setAmount(book.get().getAmount() + 1L);
                    bookRepository.save(book.get());
                }
                userBookRepository.save(userBook);
                System.out.println(userBook.getUid().toString() +" "+ userBook.getBookId().toString() +" "+ userBook.getStatus());
            }
        }
    }
}
