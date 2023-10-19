package com.example.librarymanager.Repository;

import com.example.librarymanager.Entity.MajorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MajorRepository extends JpaRepository<MajorEntity, Long> {
    MajorEntity findByMajor(String major);
}
