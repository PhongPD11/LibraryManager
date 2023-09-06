package com.example.librarymanager.Services;

import com.example.librarymanager.DTOs.ApiReponse;
import com.example.librarymanager.DTOs.BookData;
import com.example.librarymanager.Entity.BookEntity;

import java.util.ArrayList;

public interface BookService {

    ArrayList<BookData> getAllBooks();

    BookData addBook(BookData book) throws Exception;

    BookData updateBook(BookData book) throws Exception;

    String deleteBook(Long id) throws Exception;

    BookData getBookById(Long id) throws Exception;

    ArrayList<Object> getBookBySearching(String book, String author, Long authorId) throws Exception;
}
