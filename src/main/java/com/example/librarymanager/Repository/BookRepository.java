package com.example.librarymanager.Repository;

import com.example.librarymanager.Entity.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<BookEntity, Long> {
    List<BookEntity> findByName(String name);
    List<BookEntity> findByMajor(String major);
    List<BookEntity> findByType(String type);
    BookEntity findByBookId(Long bookId);
    List<BookEntity> findByNameIgnoreCaseStartsWith(String name);
    List<BookEntity> findByNameIgnoreCaseContaining(String name);
    List<BookEntity> findByBookLocation(String bookLocation);

}
