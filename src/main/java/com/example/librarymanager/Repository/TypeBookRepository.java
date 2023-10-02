package com.example.librarymanager.Repository;

import com.example.librarymanager.Entity.TypeBookEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TypeBookRepository extends JpaRepository<TypeBookEntity, Long> {
    List<TypeBookEntity> findByBookId(Long bookId);
    List<TypeBookEntity> findByTypeId(Long typeId);
}
