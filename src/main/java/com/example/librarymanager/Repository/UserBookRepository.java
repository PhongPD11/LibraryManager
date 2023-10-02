package com.example.librarymanager.Repository;

import com.example.librarymanager.Entity.UserBookEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface UserBookRepository extends JpaRepository<UserBookEntity, Long> {
    List<UserBookEntity> findByUid(Long Uid);
    UserBookEntity findByBookIdAndUid(Long bookId, Long uid);
    List<UserBookEntity> findByCreateAt(LocalDateTime createAt);
    List<UserBookEntity> findByExpireAt(LocalDateTime expireAt);
}
