package com.example.librarymanager.Controller;

import com.example.librarymanager.Commons.ApiResponse;
import com.example.librarymanager.DTOs.ApiReponse;
import com.example.librarymanager.DTOs.BookData;
import com.example.librarymanager.Services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/library")
public class BookController {

    @Autowired
    BookService service;

    @GetMapping("/books")
    public ApiReponse getAllBooks() {
        return ApiResponse.response(service.getAllBooks(), "Success!");
    }

    @PostMapping("/book")
    public ApiReponse addBook(@RequestBody BookData book) {
        try {
            return ApiResponse.response(service.addBook(book), "Success!");
        } catch (Exception e) {
            return ApiResponse.response(null, e.getMessage());
        }
    }

    @PutMapping("/book")
    public ApiReponse updateBook(@RequestBody BookData book) {
        try {
            return ApiResponse.response(service.updateBook(book), "Success!");
        } catch (Exception e) {
            return ApiResponse.response(null, e.getMessage());
        }
    }

    @DeleteMapping("/delete")
    public ApiReponse deleteBook(@RequestParam Long bookId) {
        try {
            return ApiResponse.response(service.deleteBook(bookId), "Success!");
        } catch (Exception e) {
            return ApiResponse.response(null, e.getMessage());
        }
    }

    @GetMapping("/search")
    public ApiReponse searchBook(
            @RequestParam(value = "book", required = false) String book,
            @RequestParam(value = "author", required = false) String author,
            @RequestParam(value = "bookId", required = false) Long bookId) {
        try {
            return ApiResponse.response(service.getBookBySearching(book, author, bookId), "Success!");
        } catch (Exception e) {
            return ApiResponse.response(null, e.getMessage());
        }
    }

    @GetMapping("/books/author")
    public ApiReponse getAllBooks(@RequestParam(value = "authorId") Long authorId) {
        try {
            return ApiResponse.response(service.getBookByAuthorId(authorId), "Success!");
        } catch (Exception e) {
            return ApiResponse.response(null, e.getMessage());
        }
    }

}
