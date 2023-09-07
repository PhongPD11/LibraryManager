package com.example.librarymanager.Services.Implement;

import com.example.librarymanager.Commons.BookCommons;
import com.example.librarymanager.Commons.Commons;
import com.example.librarymanager.DTOs.Book;
import com.example.librarymanager.DTOs.BookData;
import com.example.librarymanager.Entity.AuthorBookEntity;
import com.example.librarymanager.Entity.BookEntity;
import com.example.librarymanager.Repository.AuthorBookRepository;
import com.example.librarymanager.Repository.AuthorRepository;
import com.example.librarymanager.Repository.BookRepository;
import com.example.librarymanager.Services.BookService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.StringUtils;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    BookRepository bookRepository;

    @Autowired
    AuthorRepository authorRepository;

    @Autowired
    AuthorBookRepository authorBookRepository;

    @Override
    public ArrayList<Book> getAllBooks() {
        List<BookEntity> listOfBook = bookRepository.findAll();
        return BookCommons.showBooksInfo(listOfBook, authorBookRepository, authorRepository);
    }

    @Override
    public BookData addBook(BookData book) throws Exception {
        BookEntity saveBook = new BookEntity();
        if (StringUtils.isBlank(book.getName()) || book.getBookId() == null || StringUtils.isBlank(book.getType()) || book.getAmount() == null || ObjectUtils.isEmpty(book.getAuthor())) {
            throw new Exception("Please fill all information!");
        } else {
            if (bookRepository.findById(book.getBookId()).isPresent()) {
                throw new Exception("Book exist!");
            } else {
                saveBook.setName(book.getName());
                saveBook.setType(book.getType());
                saveBook.setBookId(book.getBookId());
                saveBook.setAmount(book.getAmount());

                BookCommons.saveAuthorBook(book.getAuthor(), book.getBookId(), authorBookRepository, authorRepository);

                saveBook = bookRepository.save(saveBook);
                book.setId(saveBook.getId());
                return book;
            }
        }
    }


    @Override
    public BookData updateBook(BookData book) throws Exception {
        Long id = book.getId();
        if (id != null) {
            if (bookRepository.findById(id).isPresent()) {
                BookEntity existBook = bookRepository.findById(id).get();
                Long bookIdExist = existBook.getBookId();
                String name = book.getName();
                Long amount = book.getAmount();
                String type = book.getType();

                if (book.getBookId() != null && !book.getBookId().equals(existBook.getBookId())) {
                    Long bookId = book.getBookId();
                    Optional<BookEntity> checkBook = Optional.ofNullable(bookRepository.findByBookId(bookId));
                    if (checkBook.isPresent()) {
                        throw new Exception("Book Id: " + bookId + "is owned by another book!");
                    } else {
                        existBook.setBookId(bookId);
                    }
                } else {
                    book.setBookId(existBook.getBookId());
                }

                if (StringUtils.isBlank(name)) {
                    book.setName(existBook.getName());
                } else {
                    existBook.setName(name);
                }
                if (StringUtils.isBlank(type)) {
                    book.setType(existBook.getType());
                } else {
                    existBook.setType(type);
                }
                if (amount == null) {
                    book.setAmount(existBook.getAmount());
                } else {
                    existBook.setAmount(amount);
                }
                BookCommons.updateAuthorBook(authorBookRepository.findByBookId(bookIdExist), book.getAuthor(), book.getBookId(), authorBookRepository, authorRepository);
                bookRepository.save(existBook);
                return book;
            } else throw new Exception("Book not found!");
        } else throw new Exception("Fill Book Id!");

    }

    @Override
    public String deleteBook(Long bookId) throws Exception {
        if (bookId != null) {
            Optional<BookEntity> checkBook = Optional.ofNullable(bookRepository.findByBookId(bookId));
            if (checkBook.isPresent()) {
                BookEntity existBook = checkBook.get();
                authorBookRepository.deleteAll(authorBookRepository.findByBookId(bookId));
                bookRepository.delete(existBook);
                return "Success delete " + existBook.getName();
            } else throw new Exception("Book not found!");
        } else throw new Exception("You must fill ID to delete that book!");
    }

    @Override
    public ArrayList<Book> getBookByAuthorId(Long authorId) throws Exception {
        if (authorId == null){
            throw new Exception("Fill author ID!");
        } else {
            List<AuthorBookEntity> listBooks = authorBookRepository.findByAuthorId(authorId);
            if (listBooks.isEmpty()) {
                throw new Exception("Result not found!");
            } else {
                ArrayList<BookEntity> listBooksEnt = new ArrayList<>();
                for (AuthorBookEntity book : listBooks){
                    listBooksEnt.add(bookRepository.findByBookId(book.getBookId()));
                }
                return BookCommons.showBooksInfo(listBooksEnt, authorBookRepository, authorRepository);
            }
        }
    }

    @Override
    public ArrayList<Object> getBookBySearching(String book, String author, Long bookId) throws Exception {
        ArrayList<Object> finalResults = new ArrayList<>();
        if ( StringUtils.isBlank(book) && StringUtils.isBlank(author) && bookId == null) {
            throw new Exception("Input keyword!");
        } else {
            if (bookId != null)
                finalResults.add(BookCommons.showBookInfo(bookRepository.findByBookId(bookId), authorBookRepository, authorRepository));
            else if (!Commons.isNullOrEmpty(book)){
                book = book.toLowerCase().trim();
                finalResults.addAll(BookCommons.showBooksInfo(bookRepository.findByNameIgnoreCaseStartsWith(book), authorBookRepository, authorRepository));
                finalResults.addAll(BookCommons.showBooksInfo(bookRepository.findByNameIgnoreCaseContaining(" "+book), authorBookRepository, authorRepository));
            }
            if (!Commons.isNullOrEmpty(author)){
                author = author.toLowerCase().trim();
                finalResults.addAll(BookCommons.showAuthors(authorRepository.findByAuthorNameIgnoreCaseContaining(" "+author)));
                finalResults.addAll(BookCommons.showAuthors(authorRepository.findByAuthorNameIgnoreCaseStartsWith(author)));
            }
        }
        return finalResults;
    }

}
