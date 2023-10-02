package com.example.librarymanager.Repository;

import com.example.librarymanager.Entity.TypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TypeRepository extends JpaRepository<TypeEntity, Long> {
    TypeEntity findByType(String type);
}
