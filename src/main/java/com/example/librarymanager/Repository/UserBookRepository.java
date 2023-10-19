package com.example.librarymanager.Repository;

import com.example.librarymanager.Entity.UserBookEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface UserBookRepository extends JpaRepository<UserBookEntity, Long> {
    List<UserBookEntity> findByUid(Long uid);
    List<UserBookEntity> findByBookId(Long bookId);
    UserBookEntity findByBookIdAndUid(Long bookId, Long uid);
    List<UserBookEntity> findByCreateAt(LocalDateTime createAt);
    List<UserBookEntity> findByExpireAt(LocalDateTime expireAt);

    //Borrowing
    @Query(nativeQuery = true,value ="select * from user_book where status = 'borrowing' and DATE_PART('day', expire_at - now()) <=3 and DATE_PART('day', expire_at - now()) >0 order by expire_at desc")
    List<UserBookEntity> getExpireSoon();

    @Query(nativeQuery = true,value ="select * from user_book where status = 'borrowing' and DATE_PART('day', DATE_TRUNC('day', now()) - DATE_TRUNC('day', expire_at)) > 0 order by expire_at desc")
    List<UserBookEntity> getExpired();

    //Register
    @Query(nativeQuery = true,value ="select * from user_book where status = 'registerBorrow' and DATE_PART('day',now() - create_at) <=3 and DATE_PART('day', now() - create_at) > 2 order by create_at desc")
    List<UserBookEntity> getExpiredRegisterSoon();

    @Query(nativeQuery = true,value ="select * from user_book where status = 'registerBorrow' and DATE_PART('day', DATE_TRUNC('day', now()) - DATE_TRUNC('day', create_at)) > 3 order by create_at desc")
    List<UserBookEntity> getExpiredRegister();

    //Rating
    @Query(nativeQuery = true,value ="select count(*) from user_book where rate is not null and book_id = :bookId")
    Long getUserRate(@Param("bookId") Long bookId);
}
