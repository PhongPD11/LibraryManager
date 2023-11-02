package com.example.librarymanager.Repository;

import com.example.librarymanager.Entity.UserScheduleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserScheduleRepository extends JpaRepository<UserScheduleEntity, Long> {
    List<UserScheduleEntity> findByUid(Long uid);

    @Query(nativeQuery = true, value = "SELECT * FROM user_schedule WHERE is_on = true AND hour_time = :hourTime AND minute_time = :minuteTime")
    List<UserScheduleEntity> getAlarms(@Param("hourTime") Long hourTime, @Param("minuteTime") Long minuteTime);

}
