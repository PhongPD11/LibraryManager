package com.example.librarymanager.Services.Implement;

import com.example.librarymanager.Commons.BookExist;
import com.example.librarymanager.DTOs.BookData;
import com.example.librarymanager.Entity.AuthorEntity;
import com.example.librarymanager.Entity.BookEntity;
import com.example.librarymanager.Repository.AuthorRepository;
import com.example.librarymanager.Repository.BookRepository;
import com.example.librarymanager.Services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    BookRepository bookRepository;

    @Autowired
    AuthorRepository authorRepository;


    @Override
    public ArrayList<BookEntity> getAllBooks() {
        return new ArrayList<>(bookRepository.findAll());
    }

    @Override
    public BookData addBook(BookData book) throws Exception {
        BookEntity saveBook = new BookEntity();
        AuthorEntity saveAuthor = new AuthorEntity();
        if (book.getName().isEmpty() || book.getAmount() == null || book.getAuthor().isEmpty() || book.getAuthorId() == null) {
            throw new Exception("Please fill Name, Amount, Author & Author ID!");
        } else {
            saveBook.setName(book.getName());
            saveBook.setType(book.getType());
            saveBook.setAmount(book.getAmount());
            saveBook.setAuthorId(book.getAuthorId());
            Optional<AuthorEntity> authorExist = Optional.ofNullable(authorRepository.findByAuthorId(book.getAuthorId()));
            if (!authorExist.isPresent()) {
                saveAuthor.setAuthorId(book.getAuthorId());
                saveAuthor.setAuthorName(book.getAuthor());
                authorRepository.save(saveAuthor);
            } else if (!Objects.equals(authorExist.get().getAuthorName(), book.getAuthor())) {
                throw new Exception("Add book failure. Author & Author Id invalid!");
            } else if (BookExist.isBookExist(book.getName(), book.getAuthorId(), bookRepository)) {
                throw new Exception("Add book failure. That book exist!");
            }

            saveBook = bookRepository.save(saveBook);
            book.setId(saveBook.getId());
            return book;
        }
    }

    @Override
    public BookData updateBook(BookData book) throws Exception {
        AuthorEntity saveAuthor = new AuthorEntity();
        Long id = book.getId();
        if (id != null) {
            if (bookRepository.findById(id).isPresent()) {
                BookEntity existBook = bookRepository.findById(id).get();
                String name = book.getName();
                String author = book.getAuthor();
                Long authorId = book.getAuthorId();
                Long amount = book.getAmount();
                String type = book.getType();
                if (name.isEmpty()) {
                    book.setName(existBook.getName());
                } else {
                    existBook.setName(name);
                }
                if (type.isEmpty()) {
                    book.setType(existBook.getType());
                } else {
                    existBook.setType(type);
                }
                if (amount == null) {
                    book.setAmount(existBook.getAmount());
                } else {
                    existBook.setAmount(amount);
                }

                if (authorId == null) {
                    if (author.isEmpty()) {
                        book.setAuthorId(existBook.getAuthorId());
                        Optional<AuthorEntity> authorExist = Optional.ofNullable(authorRepository.findByAuthorId(existBook.getAuthorId()));
                        authorExist.ifPresent(authorEntity -> book.setAuthor(authorEntity.getAuthorName()));
                    } else {
                        throw new Exception("You must fill Author Id to change Author Name!");
                    }
                } else {
                    Optional<AuthorEntity> existAuthor = Optional.ofNullable(authorRepository.findByAuthorId(authorId));
                    if (existAuthor.isPresent()) {
                        if (!author.isEmpty()) {
                            if (BookExist.isExistAuthor(authorId, authorRepository)){
                                existAuthor.get().setAuthorName(author);
                                existBook.setAuthorId(authorId);
                            }
                        } else {
                            book.setAuthor(existAuthor.get().getAuthorName());
                        }
                    } else {
                        if (author.isEmpty()) {
                            throw new Exception("You must fill Author Name to add new Author");
                        } else {
                            saveAuthor.setAuthorId(authorId);
                            saveAuthor.setAuthorName(author);
                            existBook.setAuthorId(authorId);
                            authorRepository.save(saveAuthor);
                        }
                    }
                }
                if (BookExist.isBookDuplicate(existBook.getName(), existBook.getAuthorId(), book.getId(), bookRepository)) {
                    throw new Exception("Update book failure. That book is duplicated!");
                } else {
                    bookRepository.save(existBook);
                    return book;
                }
            } else throw new Exception("Book not found!");
        } else throw new Exception("You must fill ID to update book!");
    }

    @Override
    public String deleteBook(Long id) throws Exception {
        if (id != null) {
            if (bookRepository.findById(id).isPresent()) {
                BookEntity existBook = bookRepository.findById(id).get();
                bookRepository.delete(existBook);
                return "Success delete " + existBook.getName();
            } else throw new Exception("Book not found!");
        } else throw new Exception("You must fill ID to delete that book!");
    }

    @Override
    public BookData getBookById(Long id) throws Exception {
        if (id != null) {
            if (bookRepository.findById(id).isPresent()) {
                BookEntity existBook = bookRepository.findById(id).get();
                BookData book = new BookData();
                book.setId(id);
                book.setName(existBook.getName());
                book.setType(existBook.getType());
                book.setAmount(existBook.getAmount());
                book.setAuthorId(existBook.getAuthorId());
                Optional<AuthorEntity> existAuthor = Optional.ofNullable(authorRepository.findByAuthorId(book.getAuthorId()));
                existAuthor.ifPresent(authorEntity -> book.setAuthor(authorEntity.getAuthorName()));
                return book;
            } else throw new Exception("Book not found!");
        } else throw new Exception("You must fill ID to find that book!");
    }
}
