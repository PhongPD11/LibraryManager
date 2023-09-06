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

    @GetMapping("/book")
    public ApiReponse getBookById(@RequestParam Long id) {
        try {
            return ApiResponse.response(service.getBookById(id), "Success!");
        } catch (Exception e) {
            return ApiResponse.response(null, e.getMessage());
        }
    }

    @PostMapping("/book")
    public ApiReponse addBook(@RequestBody BookData book) {
        try {
            return ApiResponse.response(service.addBook(book), "Success!");
        } catch (Exception e) {
            return ApiResponse.response(null, e.getMessage());
        }
    }

    @PostMapping("/test")
    public String test(@RequestBody String test) {
        return test.toUpperCase();
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
    public ApiReponse deleteBook(@RequestParam Long id) {
        try {
            return ApiResponse.response(service.deleteBook(id), "Success!");
        } catch (Exception e) {
            return ApiResponse.response(null, e.getMessage());
        }
    }

    @GetMapping("/search")
    public ApiReponse searchBook(
            @RequestParam(value = "book", required = false) String book,
            @RequestParam(value = "author", required = false) String author,
            @RequestParam(value = "authorId", required = false) Long authorId) {
        try {
            return ApiResponse.response(service.getBookBySearching(book, author, authorId), "Success!");
        } catch (Exception e) {
            return ApiResponse.response(null, e.getMessage());
        }
    }

}
