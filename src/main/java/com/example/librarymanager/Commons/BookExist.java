package com.example.librarymanager.Commons;

import com.example.librarymanager.Entity.AuthorEntity;
import com.example.librarymanager.Entity.BookEntity;
import com.example.librarymanager.Repository.AuthorRepository;
import com.example.librarymanager.Repository.BookRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class BookExist {
    public static Boolean isBookExist(String name, Long authorId, BookRepository repo){
        return findFirstBook(name, authorId, repo) != null;
    }

    public static Boolean isBookDuplicate(String name, Long authorId, Long id, BookRepository repo){
        List<BookEntity> existNameList = repo.findByName(name);
        if (!existNameList.isEmpty()) {
            boolean isDuplicate = false;
            for (BookEntity bookEntity : existNameList) {
                if (!Objects.equals(bookEntity.getId(), id) && Objects.equals(bookEntity.getAuthorId(), authorId)) {
                    isDuplicate = true;
                    break;
                }
            }
            return isDuplicate;
        } else return false;
    }

    public static BookEntity findFirstBook(String name, Long authorId, BookRepository repo){
        List<BookEntity> existNameList = repo.findByName(name);
        if (!existNameList.isEmpty()){
            Optional<BookEntity> foundItem = existNameList.stream()
                    .filter(item -> item.getAuthorId().equals(authorId))
                    .findFirst();
            return foundItem.orElse(null);
        } else return null;
    }

    public static Boolean isExistAuthor(Long authorId, AuthorRepository repo){
        Optional<AuthorEntity> authorExist = Optional.ofNullable(repo.findByAuthorId(authorId));
        return authorExist.isPresent();
    }
}
