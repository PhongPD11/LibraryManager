package com.example.librarymanager.Repository;

import com.example.librarymanager.Entity.AuthorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthorRepository extends JpaRepository<AuthorEntity, Long> {
    AuthorEntity findByAuthorId(Long authorId);

    List<AuthorEntity> findByAuthorName(String authorName);
    List<AuthorEntity> findByAuthorNameIgnoreCaseContaining(String authorName);
    List<AuthorEntity> findByAuthorNameIgnoreCaseStartsWith(String authorName);
}
