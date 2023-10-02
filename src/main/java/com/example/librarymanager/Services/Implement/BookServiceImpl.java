package com.example.librarymanager.Services.Implement;

import com.example.librarymanager.Commons.BookCommons;
import com.example.librarymanager.Commons.Commons;
import com.example.librarymanager.DTOs.Book;
import com.example.librarymanager.DTOs.BookData;
import com.example.librarymanager.Entity.AuthorBookEntity;
import com.example.librarymanager.Entity.BookEntity;
import com.example.librarymanager.Entity.TypeEntity;
import com.example.librarymanager.Entity.UserBookEntity;
import com.example.librarymanager.Repository.*;
import com.example.librarymanager.Services.BookService;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.librarymanager.Commons.BookCommons.BOOK_NOT_FOUND;
import static com.example.librarymanager.Commons.Commons.*;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    BookRepository bookRepository;

    @Autowired
    AuthorRepository authorRepository;

    @Autowired
    TypeRepository typeRepository;

    @Autowired
    TypeBookRepository typeBookRepository;

    @Autowired
    AuthorBookRepository authorBookRepository;

    @Autowired
    UserBookRepository userBookRepository;

    @Override
    public ArrayList<Book> getAllBooks() {
        List<BookEntity> listOfBook = bookRepository.findAll();
        return BookCommons.showBooksInfo(listOfBook, authorBookRepository, authorRepository, typeBookRepository, typeRepository);
    }

    @Override
    public BookData addBook(BookData book) throws Exception {
        BookEntity saveBook = new BookEntity();
        if (StringUtils.isBlank(book.getName()) || book.getBookId() == null ||
                StringUtils.isBlank(book.getName()) || StringUtils.isBlank(book.getName()) ||
                book.getBorrowingPeriod() == null || ObjectUtils.isEmpty(book.getAuthor()) ||
                StringUtils.isBlank(book.getBookLocation())) {
            throw new Exception("Please fill all information!");
        } else {
            Optional<BookEntity> existBook = Optional.ofNullable(bookRepository.findByBookId(book.getBookId()));
            if (existBook.isPresent()) {
                throw new Exception("Book exist!");
            } else {
                saveBook.setName(book.getName());
                saveBook.setBookId(book.getBookId());
                saveBook.setAmount(book.getAmount());
                saveBook.setBorrowingPeriod(book.getBorrowingPeriod());
                saveBook.setBookLocation(book.getBookLocation());

                BookCommons.saveAuthorBook(book.getAuthor(), book.getBookId(), authorBookRepository, authorRepository);
                BookCommons.saveTypeBook(book.getType(), book.getBookId(), typeBookRepository, typeRepository);

                saveBook = bookRepository.save(saveBook);
                book.setId(saveBook.getId());

                return book;
            }
        }
    }

    @Override
    public String addType(String type) throws Exception {
        JSONObject jsonObject = new JSONObject(type);
        String typeName = jsonObject.getString("type");
        if (StringUtils.isNotBlank(typeName)) {
            TypeEntity existType = typeRepository.findByType(typeName);
            if (existType == null) {
                TypeEntity newType = new TypeEntity();
                newType.setType(typeName);
                typeRepository.save(newType);
                return SUCCESS;
            } else {
                throw new Exception(EXIST);
            }
        } else throw new Exception(DATA_NULL);
    }

    @Override
    public Book getBookDetail(Long bookId) throws Exception {
        if (bookId != null) {
            BookEntity book = bookRepository.findByBookId(bookId);
            if (book != null) {
                return BookCommons.showBookInfo(book, authorBookRepository, authorRepository, typeBookRepository, typeRepository);
            } else throw new Exception(BOOK_NOT_FOUND);
        } else throw new Exception(UID_NULL);
    }

    @Override
    public UserBookEntity scheduleBorrow(Long bookId, Long uid) throws Exception {
        if (bookId != null && uid !=null) {
            Optional<BookEntity> existBook = Optional.ofNullable(bookRepository.findByBookId(bookId));
            if (existBook.isPresent()) {
                Long amount = existBook.get().getAmount();
                if (amount > 0) {
                    Optional<UserBookEntity> existLoan = Optional.ofNullable(userBookRepository.findByBookIdAndUid(bookId, uid));
                    if (existLoan.isPresent()) {
                        throw new Exception(EXIST_LOAN);
                    } else {
                        existBook.get().setAmount(amount - 1L);
                        bookRepository.save(existBook.get());
                        UserBookEntity borrow = new UserBookEntity();
                        borrow.setBookId(bookId);
                        borrow.setUid(uid);
                        LocalDateTime currentTime = LocalDateTime.now();
                        borrow.setCreateAt(currentTime);
                        borrow.setStatus(SCHEDULE_BORROW);
                        userBookRepository.save(borrow);
                        return borrow;
                    }
                } else throw new Exception(NOT_AVAILABLE);
            } else throw new Exception(NOT_EXIST);
        } else throw new Exception(DATA_NULL);
    }

    @Override
    public UserBookEntity borrowBook(Long bookId, Long uid) throws Exception {
        if (bookId != null && uid !=null) {
            Optional<BookEntity> existBook = Optional.ofNullable(bookRepository.findByBookId(bookId));
            if (existBook.isPresent()) {
                LocalDateTime currentTime = LocalDateTime.now();
                LocalDateTime expireTime = currentTime.plusDays(existBook.get().getBorrowingPeriod());
                Optional<UserBookEntity> existBorrow = Optional.ofNullable(userBookRepository.findByBookIdAndUid(bookId, uid));
                if (existBorrow.isPresent()) {
                    //Borrow after registering
                    existBorrow.get().setCreatedAt(currentTime);
                    existBorrow.get().setExpireAt(expireTime);
                    existBorrow.get().setStatus(BORROWING);
                    userBookRepository.save(existBorrow.get());
                    return existBorrow.get();
                } else {
                    //Borrow directly
                    Long amount = existBook.get().getAmount();
                    if (amount > 0) {
                        existBook.get().setAmount(amount - 1L);
                        bookRepository.save(existBook.get());
                        UserBookEntity borrow = new UserBookEntity();
                        borrow.setBookId(bookId);
                        borrow.setUid(uid);
                        borrow.setCreateAt(currentTime);
                        borrow.setCreatedAt(currentTime);
                        borrow.setExpireAt(expireTime);
                        borrow.setStatus(BORROWING);
                        userBookRepository.save(borrow);
                        return borrow;
                    } else throw new Exception(NOT_AVAILABLE);
                }
            } else throw new Exception(NOT_EXIST);
        } else throw new Exception(DATA_NULL);
    }

    @Override
    public String returnBook(Long bookId, Long uid) throws Exception {
        if (bookId != null && uid !=null){
            Optional<UserBookEntity> existBorrow = Optional.ofNullable(userBookRepository.findByBookIdAndUid(bookId, uid));
            Optional<BookEntity> existBook = Optional.ofNullable(bookRepository.findByBookId(bookId));
            LocalDateTime currentTime = LocalDateTime.now();
            if (existBorrow.isPresent() && existBook.isPresent()){
                existBorrow.get().setReturnedAt(currentTime);
                existBorrow.get().setStatus(BORROW_RETURNED);
                existBook.get().setAmount(existBook.get().getAmount() + 1L);
                userBookRepository.save(existBorrow.get());
                bookRepository.save(existBook.get());
                return SUCCESS;
            } else throw new Exception(NOT_EXIST);
        } else throw new Exception(DATA_NULL);
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
                if (amount == null) {
                    book.setAmount(existBook.getAmount());
                } else {
                    existBook.setAmount(amount);
                }

                typeBookRepository.deleteAll(typeBookRepository.findByBookId(bookIdExist));
                BookCommons.saveTypeBook(book.getType(), book.getBookId(), typeBookRepository, typeRepository);

                authorBookRepository.deleteAll(authorBookRepository.findByBookId(bookIdExist));
                BookCommons.saveAuthorBook(book.getAuthor(), book.getBookId(), authorBookRepository, authorRepository);

                bookRepository.save(existBook);
                return book;
            } else throw new Exception("Book not found!");
        } else throw new Exception("Fill Id!");

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
        if (authorId == null) {
            throw new Exception("Fill author ID!");
        } else {
            List<AuthorBookEntity> listBooks = authorBookRepository.findByAuthorId(authorId);
            if (listBooks.isEmpty()) {
                throw new Exception("Result not found!");
            } else {
                ArrayList<BookEntity> listBooksEnt = new ArrayList<>();
                for (AuthorBookEntity book : listBooks) {
                    listBooksEnt.add(bookRepository.findByBookId(book.getBookId()));
                }
                return BookCommons.showBooksInfo(listBooksEnt, authorBookRepository, authorRepository, typeBookRepository, typeRepository);
            }
        }
    }

    @Override
    public ArrayList<Object> getBookBySearching(String book, String author, Long bookId) throws Exception {
        ArrayList<Object> finalResults = new ArrayList<>();
        if (StringUtils.isBlank(book) && StringUtils.isBlank(author) && bookId == null) {
            throw new Exception("Input keyword!");
        } else {
            if (bookId != null)
                finalResults.add(BookCommons.showBookInfo(bookRepository.findByBookId(bookId), authorBookRepository, authorRepository, typeBookRepository, typeRepository));
            else if (!Commons.isNullOrEmpty(book)) {
                book = book.toLowerCase().trim();
                finalResults.addAll(BookCommons.showBooksInfo(bookRepository.findByNameIgnoreCaseStartsWith(book), authorBookRepository, authorRepository, typeBookRepository, typeRepository));
                finalResults.addAll(BookCommons.showBooksInfo(bookRepository.findByNameIgnoreCaseContaining(" " + book), authorBookRepository, authorRepository, typeBookRepository, typeRepository));
            }
            if (!Commons.isNullOrEmpty(author)) {
                author = author.toLowerCase().trim();
                finalResults.addAll(BookCommons.showAuthors(authorRepository.findByAuthorNameIgnoreCaseContaining(" " + author)));
                finalResults.addAll(BookCommons.showAuthors(authorRepository.findByAuthorNameIgnoreCaseStartsWith(author)));
            }
        }
        return finalResults;
    }

}
