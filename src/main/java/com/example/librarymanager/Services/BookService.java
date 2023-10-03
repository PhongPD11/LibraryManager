package com.example.librarymanager.Services;

import com.example.librarymanager.DTOs.Book;
import com.example.librarymanager.DTOs.BookData;
import com.example.librarymanager.DTOs.BorrowBook;
import com.example.librarymanager.Entity.UserBookEntity;

import java.util.ArrayList;
import java.util.List;

public interface BookService {

    ArrayList<Book> getAllBooks();

    BookData addBook(BookData book) throws Exception;

    BookData updateBook(BookData book) throws Exception;

    String deleteBook(Long bookId) throws Exception;

    ArrayList<Book> getBookByAuthorId(Long authorId) throws Exception;

    ArrayList<Object> getBookBySearching(String book, String author, Long authorId) throws Exception;

    String addType(String type) throws Exception;

    Book getBookDetail(Long bookId) throws Exception;

    UserBookEntity scheduleBorrow(BorrowBook borrow) throws Exception;
    UserBookEntity borrowBook(Long bookId, Long uid) throws Exception;
    String returnBook(Long bookId, Long uid) throws Exception;
    String voteBook(Long bookId, Long uid, Integer star) throws Exception;
    String favoriteBook(Long bookId, Long uid, Boolean isFavorite) throws Exception;
    List<Book> favoriteBooks(Long uid) throws Exception;
}
