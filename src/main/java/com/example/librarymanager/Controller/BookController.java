package com.example.librarymanager.Controller;

import com.example.librarymanager.Commons.ResponseCommon;
import com.example.librarymanager.DTOs.ApiResponse;
import com.example.librarymanager.DTOs.BookData;
import com.example.librarymanager.DTOs.BorrowBook;
import com.example.librarymanager.Repository.BookRepository;
import com.example.librarymanager.Services.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

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
    public ApiResponse addBook(@RequestPart("model") String json, @RequestPart("file") MultipartFile file) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            BookData dataModel = mapper.readValue(json, BookData.class);
            return ResponseCommon.response(service.addBook(dataModel, file), "Success!");
        } catch (Exception e) {
            return ResponseCommon.response(null, e.getMessage());
        }
    }

    @GetMapping("/book")
    public ApiResponse getBook(@RequestParam Long bookId) {
        try {
            return ResponseCommon.response(service.getBookDetail(bookId), "Success!");
        } catch (Exception e) {
            return ResponseCommon.response(null, e.getMessage());
        }
    }

    @PostMapping("/type")
    public ApiResponse addType(@RequestBody String type) {
        try {
            return ResponseCommon.response(service.addType(type), "Success!");
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

    @Autowired
    BookRepository bookRepository;

    @GetMapping("/test")
    public List<String> Test(){
        List<String> majors = bookRepository.findDistinctMajors();
        List<String> types = bookRepository.findDistinctTypes();
        ArrayList<String> field = new ArrayList<String>();
        field.addAll(majors);
        field.addAll(types);
        return field;
    }

    @PostMapping("/borrow/register")
    public ApiResponse registerBorrow(@RequestBody BorrowBook borrow){
        try {
            return ResponseCommon.response(service.registerBorrow(borrow), "Success!");
        } catch (Exception e) {
            return ResponseCommon.response(null, e.getMessage());
        }
    }
    @GetMapping("/borrow")
    public ApiResponse loanBook(@RequestParam Long bookId, Long uid){
        try {
            return ResponseCommon.response(service.borrowBook(bookId, uid), "Success!");
        } catch (Exception e) {
            return ResponseCommon.response(null, e.getMessage());
        }
    }
    @GetMapping("/borrow/return")
    public ApiResponse returnBook(@RequestParam Long bookId, Long uid){
        try {
            return ResponseCommon.response(service.returnBook(bookId, uid), "Success!");
        } catch (Exception e) {
            return ResponseCommon.response(null, e.getMessage());
        }
    }

    @GetMapping("/rate")
    public ApiResponse voteBook(@RequestParam Long bookId, Long uid, Integer star){
        try {
            return ResponseCommon.response(service.rateBook(bookId, uid, star), "Success!");
        } catch (Exception e) {
            return ResponseCommon.response(null, e.getMessage());
        }
    }
    @GetMapping("/favorite")
    public ApiResponse favoriteBook(@RequestParam Long bookId, Long uid, Boolean isFavorite){
        try {
            return ResponseCommon.response(service.favoriteBook(bookId, uid, isFavorite), "Success!");
        } catch (Exception e) {
            return ResponseCommon.response(null, e.getMessage());
        }
    }
    @GetMapping("/favorites")
    public ApiResponse getFavoriteBooks(@RequestParam Long uid){
        try {
            return ResponseCommon.response(service.favoriteBooks(uid), "Success!");
        } catch (Exception e) {
            return ResponseCommon.response(null, e.getMessage());
        }
    }

    @GetMapping("/books/top")
    public ApiResponse getTopBooks(){
        try {
            return ResponseCommon.response(service.topBooks(), "Success!");
        } catch (Exception e) {
            return ResponseCommon.response(null, e.getMessage());
        }
    }

    @GetMapping("/userbook")
    public ApiResponse getUserBook(@RequestParam Long uid){
        try {
            return ResponseCommon.response(service.userBook(uid), "Success!");
        } catch (Exception e) {
            return ResponseCommon.response(null, e.getMessage());
        }
    }

}
