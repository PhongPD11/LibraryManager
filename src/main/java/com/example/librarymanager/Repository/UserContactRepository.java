package com.example.librarymanager.Repository;

import com.example.librarymanager.Entity.UserContactEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserContactRepository extends JpaRepository<UserContactEntity, Long> {
    List<UserContactEntity> findByUid(Long uid);

}
