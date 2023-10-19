package com.example.librarymanager.Schedule;

import com.example.librarymanager.Entity.BookEntity;
import com.example.librarymanager.Entity.UserBookEntity;
import com.example.librarymanager.Repository.BookRepository;
import com.example.librarymanager.Repository.UserBookRepository;
import com.example.librarymanager.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

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

    @Scheduled(cron = "10 * * * * ?")
    public void cronJobSch3() {
        List<UserBookEntity> listExpireSoon = userBookRepository.getExpiredRegisterSoon();
        if (!listExpireSoon.isEmpty()){
            for (UserBookEntity userBook : listExpireSoon){
                System.out.println(userBook.getUid() + " " + userBook.getBookId());
            }
        }
    }

    @Scheduled(cron = "15 * * * * ?")
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
