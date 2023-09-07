package com.example.librarymanager.Services;

import com.example.librarymanager.DTOs.ApiReponse;
import com.example.librarymanager.DTOs.Book;
import com.example.librarymanager.DTOs.BookData;
import com.example.librarymanager.Entity.BookEntity;

import java.util.ArrayList;

public interface BookService {

    ArrayList<Book> getAllBooks();

    BookData addBook(BookData book) throws Exception;

    BookData updateBook(BookData book) throws Exception;

    String deleteBook(Long bookId) throws Exception;

    ArrayList<Book> getBookByAuthorId(Long authorId) throws Exception;

    ArrayList<Object> getBookBySearching(String book, String author, Long authorId) throws Exception;
}
