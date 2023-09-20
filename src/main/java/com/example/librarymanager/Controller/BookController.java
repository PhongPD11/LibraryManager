package com.example.librarymanager.Controller;

import com.example.librarymanager.Commons.ResponseCommon;
import com.example.librarymanager.DTOs.ApiResponse;
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
    public ApiResponse getAllBooks() {
        return ResponseCommon.response(service.getAllBooks(), "Success!");
    }

    @PostMapping("/book")
    public ApiResponse addBook(@RequestBody BookData book) {
        try {
            return ResponseCommon.response(service.addBook(book), "Success!");
        } catch (Exception e) {
            return ResponseCommon.response(null, e.getMessage());
        }
    }

    @PutMapping("/book")
    public ApiResponse updateBook(@RequestBody BookData book) {
        try {
            return ResponseCommon.response(service.updateBook(book), "Success!");
        } catch (Exception e) {
            return ResponseCommon.response(null, e.getMessage());
        }
    }

    @DeleteMapping("/delete")
    public ApiResponse deleteBook(@RequestParam Long bookId) {
        try {
            return ResponseCommon.response(service.deleteBook(bookId), "Success!");
        } catch (Exception e) {
            return ResponseCommon.response(null, e.getMessage());
        }
    }

    @GetMapping("/search")
    public ApiResponse searchBook(
            @RequestParam(value = "book", required = false) String book,
            @RequestParam(value = "author", required = false) String author,
            @RequestParam(value = "bookId", required = false) Long bookId) {
        try {
            return ResponseCommon.response(service.getBookBySearching(book, author, bookId), "Success!");
        } catch (Exception e) {
            return ResponseCommon.response(null, e.getMessage());
        }
    }

    @GetMapping("/books/author")
    public ApiResponse getAllBooks(@RequestParam(value = "authorId") Long authorId) {
        try {
            return ResponseCommon.response(service.getBookByAuthorId(authorId), "Success!");
        } catch (Exception e) {
            return ResponseCommon.response(null, e.getMessage());
        }
    }

    @GetMapping("/test")
    public String Test(){
        return "Update!";
    }

}
