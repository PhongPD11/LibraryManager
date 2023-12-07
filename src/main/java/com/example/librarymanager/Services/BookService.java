package com.example.librarymanager.Services;

import com.example.librarymanager.DTOs.Book;
import com.example.librarymanager.DTOs.BookData;
import com.example.librarymanager.DTOs.BorrowBook;
import com.example.librarymanager.Entity.AuthorEntity;
import com.example.librarymanager.Entity.UserBookEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

public interface BookService {
    List<AuthorEntity> getAuthors();

    ArrayList<Book> getAllBooks();

    BookData addBook(BookData book, MultipartFile file) throws Exception;

    BookData updateBook(BookData book, MultipartFile file) throws Exception;

    String deleteBook(Long bookId) throws Exception;

    ArrayList<Book> getBookByAuthor(String authorName, Long authorId) throws Exception;

    ArrayList<Object> getBookBySearching(String book, String author, Long authorId) throws Exception;
    ArrayList<Book> getBookByType(String type) throws Exception;
    ArrayList<Book> getBookByMajor(String major) throws Exception;
    ArrayList<Book> getByLanguage(String language) throws Exception;

    String addType(String type) throws Exception;

    Book getBookDetail(Long bookId) throws Exception;

    UserBookEntity registerBorrow(BorrowBook borrow) throws Exception;
    String borrowBook(Long bookId, Long uid) throws Exception;
    String returnBook(Long bookId, Long uid) throws Exception;
    String changeStatusUserBook(UserBookEntity userBook) throws Exception;
    String rateBook(Long bookId, Long uid, Integer star) throws Exception;
    String favoriteBook(Long bookId, Long uid, Boolean isFavorite) throws Exception;
    List<Book> favoriteBooks(Long uid) throws Exception;
    List<Book> topBooks() throws Exception;
    List<UserBookEntity> userBook(Long uid) throws Exception;

    List<UserBookEntity> getUserBook() throws Exception;
}
