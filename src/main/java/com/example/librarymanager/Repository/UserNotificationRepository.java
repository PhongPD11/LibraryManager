package com.example.librarymanager.Repository;

import com.example.librarymanager.Entity.UserNotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserNotificationRepository extends JpaRepository<UserNotificationEntity, Long> {
    List<UserNotificationEntity> findByUid(Long uid);
    List<UserNotificationEntity> findByTitle(String title);

}
