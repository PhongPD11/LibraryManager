package com.example.librarymanager.Repository;

import com.example.librarymanager.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByUsername(String username);
    UserEntity findByEmail(String email);
    UserEntity findByUid(Long uid);
    List<UserEntity> findAllByIsAdminAndIsEnabled(Boolean isAdmin, Boolean isEnabled);
}
