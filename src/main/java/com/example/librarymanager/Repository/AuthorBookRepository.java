package com.example.librarymanager.Repository;

import com.example.librarymanager.Entity.AuthorBookEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthorBookRepository extends JpaRepository<AuthorBookEntity, Long> {
    List<AuthorBookEntity> findByBookId(Long bookId);
    List<AuthorBookEntity> findByAuthorId(Long authorId);
}
