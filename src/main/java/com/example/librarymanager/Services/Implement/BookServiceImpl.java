package com.example.librarymanager.Services.Implement;

import com.example.librarymanager.Commons.BookCommons;
import com.example.librarymanager.Commons.Commons;
import com.example.librarymanager.DTOs.Book;
import com.example.librarymanager.DTOs.BookData;
import com.example.librarymanager.DTOs.BorrowBook;
import com.example.librarymanager.Entity.*;
import com.example.librarymanager.Repository.*;
import com.example.librarymanager.Services.BookService;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.example.librarymanager.Commons.BookCommons.BOOK_NOT_FOUND;
import static com.example.librarymanager.Commons.BookCommons.showBooksInfo;
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
    MajorRepository majorRepository;

    @Autowired
    AuthorBookRepository authorBookRepository;

    @Autowired
    UserBookRepository userBookRepository;

    @Override
    public ArrayList<Book> getAllBooks() {
        Sort.Order order = new Sort.Order(Sort.Direction.ASC, "name");
        Sort sort = Sort.by(order);
        List<BookEntity> listOfBook = bookRepository.findAll(sort);
        return BookCommons.showBooksInfo(listOfBook, authorBookRepository, authorRepository, userBookRepository);
    }

    @Override
    public BookData addBook(BookData book, MultipartFile file) throws Exception {
        BookEntity saveBook = new BookEntity();
        if (StringUtils.isBlank(book.getName()) || book.getBookId() == null ||
                StringUtils.isBlank(book.getName()) || StringUtils.isBlank(book.getName()) ||
                StringUtils.isBlank(book.getLanguage()) || StringUtils.isBlank(book.getType()) ||
                StringUtils.isBlank(book.getMajor()) || ObjectUtils.isEmpty(book.getAuthor()) ||
                StringUtils.isBlank(book.getDdc()) || book.getPublicationYear() == null ||
                book.getBorrowingPeriod() == null || StringUtils.isBlank(book.getBookLocation()) ||
                StringUtils.isBlank(book.getStatus())) {
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
                saveBook.setLanguage(book.getLanguage());
                saveBook.setDdc(book.getDdc());
                saveBook.setPublicationYear(book.getPublicationYear());
                saveBook.setStatus(book.getStatus());
                saveBook.setRated(0.0);

                BookCommons.saveMajor(majorRepository, book.getMajor());
                BookCommons.saveType(typeRepository, book.getType());

                saveBook.setType(book.getType());
                saveBook.setMajor(book.getMajor());

                BookCommons.saveAuthorBook(book.getAuthor(), book.getBookId(), authorBookRepository, authorRepository);
//                BookCommons.saveTypeBook(book.getType(), book.getBookId(), typeBookRepository, typeRepository);

                if (file != null) {
                    String url = Commons.uploadImage(file, "book_image/");
                    if (StringUtils.isNotBlank(url)) {
                        saveBook.setImageUrl(url);
                        book.setImageUrl(url);
                    }
                } else throw new Exception("Pls upload image!");

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
            } else throw new Exception(EXIST);
        } else throw new Exception(DATA_NULL);
    }

    @Override
    public Book getBookDetail(Long bookId) throws Exception {
        if (bookId != null) {
            BookEntity book = bookRepository.findByBookId(bookId);
            if (book != null) {
                return BookCommons.showBookInfo(book, authorBookRepository, authorRepository, userBookRepository);
            } else throw new Exception(BOOK_NOT_FOUND);
        } else throw new Exception(UID_NULL);
    }

    @Override
    public UserBookEntity registerBorrow(BorrowBook borrow) throws Exception {
        if (borrow.getBookId() != null && borrow.getUid() != null && borrow.getIsDelivery() != null) {
            Long bookId = borrow.getBookId();
            Long uid = borrow.getUid();
            Optional<BookEntity> existBook = Optional.ofNullable(bookRepository.findByBookId(bookId));
            if (existBook.isPresent()) {
                Long amount = existBook.get().getAmount();
                if (amount > 0) {
                    Optional<UserBookEntity> existUserBook = Optional.ofNullable(userBookRepository.findByBookIdAndUid(bookId, uid));
                    if (existUserBook.isPresent()) {
                        if (Objects.equals(existUserBook.get().getStatus(), BORROW_RETURNED) || StringUtils.isBlank(existUserBook.get().getStatus())) {
                            UserBookEntity reBorrow = existUserBook.get();
                            existBook.get().setAmount(amount - 1L);
                            bookRepository.save(existBook.get());
                            reBorrow.setBookId(bookId);
                            reBorrow.setUid(uid);
                            reBorrow.setIsDelivery(borrow.getIsDelivery());
                            if (borrow.getIsDelivery()) {
                                if (StringUtils.isNotBlank(borrow.getAddress())) {
                                    reBorrow.setAddress(borrow.getAddress());
                                } else throw new Exception(DATA_NULL);
                            }
                            LocalDateTime currentTime = LocalDateTime.now();
                            reBorrow.setCreateAt(currentTime);
                            reBorrow.setStatus(REGISTER_BORROW);
                            userBookRepository.save(reBorrow);
                            return reBorrow;
                        }
                        throw new Exception(EXIST_BORROW);
                    } else {
                        existBook.get().setAmount(amount - 1L);
                        bookRepository.save(existBook.get());
                        UserBookEntity newBorrow = new UserBookEntity();
                        newBorrow.setBookId(bookId);
                        newBorrow.setUid(uid);
                        newBorrow.setIsDelivery(borrow.getIsDelivery());
                        if (borrow.getIsDelivery()) {
                            if (StringUtils.isNotBlank(borrow.getAddress())) {
                                newBorrow.setAddress(borrow.getAddress());
                            } else throw new Exception(DATA_NULL);
                        }
                        LocalDateTime currentTime = LocalDateTime.now();
                        newBorrow.setCreateAt(currentTime);
                        newBorrow.setStatus(REGISTER_BORROW);
                        userBookRepository.save(newBorrow);
                        return newBorrow;
                    }
                } else throw new Exception(NOT_AVAILABLE);
            } else throw new Exception(NOT_EXIST);
        } else throw new Exception(DATA_NULL);
    }

    @Override
    public UserBookEntity borrowBook(Long bookId, Long uid) throws Exception {
        if (bookId != null && uid != null) {
            Optional<BookEntity> existBook = Optional.ofNullable(bookRepository.findByBookId(bookId));
            if (existBook.isPresent()) {
                LocalDateTime currentTime = LocalDateTime.now();
                LocalDateTime expireTime = currentTime.plusDays(existBook.get().getBorrowingPeriod());
                Optional<UserBookEntity> existBorrow = Optional.ofNullable(userBookRepository.findByBookIdAndUid(bookId, uid));
                if (existBorrow.isPresent()) {
                    if (!Objects.equals(existBorrow.get().getStatus(), BORROWING)) {
                        existBorrow.get().setCreatedAt(currentTime);
                        existBorrow.get().setExpireAt(expireTime);
                        existBorrow.get().setStatus(BORROWING);
                        userBookRepository.save(existBorrow.get());
                    }
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
        if (bookId != null && uid != null) {
            Optional<UserBookEntity> existBorrow = Optional.ofNullable(userBookRepository.findByBookIdAndUid(bookId, uid));
            Optional<BookEntity> existBook = Optional.ofNullable(bookRepository.findByBookId(bookId));
            LocalDateTime currentTime = LocalDateTime.now();
            if (existBorrow.isPresent() && existBook.isPresent()) {
                if (!Objects.equals(existBorrow.get().getStatus(), BORROW_RETURNED)) {
                    existBorrow.get().setReturnedAt(currentTime);
                    existBorrow.get().setStatus(BORROW_RETURNED);
                    existBook.get().setAmount(existBook.get().getAmount() + 1L);
                    userBookRepository.save(existBorrow.get());
                    bookRepository.save(existBook.get());
                    return SUCCESS;
                } else throw new Exception("Book had returned");
            } else throw new Exception(NOT_EXIST);
        } else throw new Exception(DATA_NULL);
    }

    @Override
    public String rateBook(Long bookId, Long uid, Integer star) throws Exception {
        if (bookId != null && uid != null && star != null) {
            if (star >= 1 && star <= 5) {
                Optional<BookEntity> existBook = Optional.ofNullable(bookRepository.findByBookId(bookId));
                if (existBook.isPresent()) {
                    Optional<UserBookEntity> existUserBook = Optional.ofNullable(userBookRepository.findByBookIdAndUid(bookId, uid));
                    if (existUserBook.isPresent()) {
                        existUserBook.get().setRate(star);
                        userBookRepository.save(existUserBook.get());
                    } else {
                        UserBookEntity newVote = new UserBookEntity();
                        newVote.setRate(star);
                        newVote.setUid(uid);
                        newVote.setBookId(bookId);
                        userBookRepository.save(newVote);
                    }
                    BookCommons.voteBook(userBookRepository, bookRepository, bookId, existBook.get());
                    return SUCCESS;
                } else throw new Exception(NOT_EXIST);
            } else throw new Exception(INVALID);
        } else throw new Exception(DATA_NULL);
    }

    @Override
    public String favoriteBook(Long bookId, Long uid, Boolean isFavorite) throws Exception {
        if (bookId != null && uid != null && isFavorite != null) {
            Optional<BookEntity> existBook = Optional.ofNullable(bookRepository.findByBookId(bookId));
            if (existBook.isPresent()) {
                Optional<UserBookEntity> existUserBook = Optional.ofNullable(userBookRepository.findByBookIdAndUid(bookId, uid));
                if (existUserBook.isPresent()) {
                    existUserBook.get().setIsFavorite(isFavorite);
                    userBookRepository.save(existUserBook.get());
                } else {
                    UserBookEntity newVote = new UserBookEntity();
                    newVote.setIsFavorite(isFavorite);
                    newVote.setUid(uid);
                    newVote.setBookId(bookId);
                    userBookRepository.save(newVote);
                }
                return SUCCESS;
            } else throw new Exception(NOT_EXIST);
        } else throw new Exception(DATA_NULL);
    }

    @Override
    public List<Book> favoriteBooks(Long uid) throws Exception {
        if (uid != null) {
            List<UserBookEntity> listBook = userBookRepository.findByUid(uid);
            if (listBook.isEmpty()) {
                throw new Exception(EMPTY);
            } else {
                ArrayList<BookEntity> favoriteBooks = new ArrayList<>();
                for (UserBookEntity book : listBook) {
                    if (book.getIsFavorite() != null && book.getIsFavorite()) {
                        Long bookId = book.getBookId();
                        BookEntity bookExist = bookRepository.findByBookId(bookId);
                        if (bookExist != null) {
                            favoriteBooks.add(bookExist);
                        } else throw new Exception(BOOK_NOT_FOUND);
                    }
                }
                return BookCommons.showBooksInfo(favoriteBooks, authorBookRepository, authorRepository, userBookRepository);
            }
        } else throw new Exception(DATA_NULL);
    }

    @Override
    public List<Book> topBooks() throws Exception {
        List<BookEntity> topBooks = bookRepository.getTopBooks();
        if (!topBooks.isEmpty()) {
            return showBooksInfo(topBooks, authorBookRepository, authorRepository, userBookRepository);
        } else throw new Exception(NOT_EXIST);
    }

    @Override
    public List<UserBookEntity> userBook(Long uid) throws Exception {
        if (uid != null) {
            return userBookRepository.findByUid(uid);
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
                String major = book.getMajor();
                String type = book.getType();
                String language = book.getLanguage();
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
                if (StringUtils.isBlank(language)) {
                    book.setLanguage(existBook.getLanguage());
                } else {
                    existBook.setName(language);
                }
                if (StringUtils.isBlank(type)) {
                    book.setType(existBook.getType());
                } else {
                    existBook.setName(type);
                }
                if (StringUtils.isBlank(major)) {
                    book.setMajor(existBook.getMajor());
                } else {
                    existBook.setMajor(major);
                }
                if (amount == null) {
                    book.setAmount(existBook.getAmount());
                } else {
                    existBook.setAmount(amount);
                }

//                typeBookRepository.deleteAll(typeBookRepository.findByBookId(bookIdExist));
//                BookCommons.saveTypeBook(book.getType(), book.getBookId(), typeBookRepository, typeRepository);

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
    public ArrayList<Book> getBookByAuthor(String authorName, Long authorId) throws Exception {
        if (authorId == null) {
            if (StringUtils.isBlank(authorName)) {
                throw new Exception(DATA_NULL);
            } else {
                List<AuthorEntity> listAuths = authorRepository.findByAuthorName(authorName);
                if (listAuths.isEmpty()) {
                    throw new Exception("Author not found!");
                } else {
                    ArrayList<BookEntity> listBooksEnt = new ArrayList<>();
                    for (AuthorEntity auth : listAuths) {
                        List<AuthorBookEntity> listBooks = authorBookRepository.findByAuthorId(auth.getAuthorId());
                        if (!listBooks.isEmpty()) {
                            for (AuthorBookEntity book : listBooks) {
                                listBooksEnt.add(bookRepository.findByBookId(book.getBookId()));
                            }
                        }
                    }
                    if (listBooksEnt.isEmpty())
                        throw new Exception("Author not found!");
                    else
                        return BookCommons.showBooksInfo(listBooksEnt, authorBookRepository, authorRepository, userBookRepository);
                }
            }
        } else {
            List<AuthorBookEntity> listBooks = authorBookRepository.findByAuthorId(authorId);
            if (listBooks.isEmpty()) {
                throw new Exception("Result not found!");
            } else {
                ArrayList<BookEntity> listBooksEnt = new ArrayList<>();
                for (AuthorBookEntity book : listBooks) {
                    listBooksEnt.add(bookRepository.findByBookId(book.getBookId()));
                }
                return BookCommons.showBooksInfo(listBooksEnt, authorBookRepository, authorRepository, userBookRepository);
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
                finalResults.add(BookCommons.showBookInfo(bookRepository.findByBookId(bookId), authorBookRepository, authorRepository, userBookRepository));
            else if (!Commons.isNullOrEmpty(book)) {
                book = book.toLowerCase().trim();
                finalResults.addAll(BookCommons.showBooksInfo(bookRepository.findByNameIgnoreCaseStartsWith(book), authorBookRepository, authorRepository, userBookRepository));
                finalResults.addAll(BookCommons.showBooksInfo(bookRepository.findByNameIgnoreCaseContaining(" " + book), authorBookRepository, authorRepository, userBookRepository));
            }
            if (!Commons.isNullOrEmpty(author)) {
                author = author.toLowerCase().trim();
                finalResults.addAll(BookCommons.showAuthors(authorRepository.findByAuthorNameIgnoreCaseContaining(" " + author)));
                finalResults.addAll(BookCommons.showAuthors(authorRepository.findByAuthorNameIgnoreCaseStartsWith(author)));
            }
        }
        return finalResults;
    }

    @Override
    public ArrayList<Book> getBookByType(String type) throws Exception {
        if (StringUtils.isBlank(type)) {
            throw new Exception(DATA_NULL);
        } else {
            List<BookEntity> listBooks = bookRepository.findByType(type);
            if (listBooks.isEmpty()) {
                throw new Exception(NOT_EXIST);
            } else {
                return BookCommons.showBooksInfo(listBooks, authorBookRepository, authorRepository, userBookRepository);
            }
        }
    }

    @Override
    public ArrayList<Book> getBookByMajor(String major) throws Exception {
        if (StringUtils.isBlank(major)) {
            throw new Exception(DATA_NULL);
        } else {
            List<BookEntity> listBooks = bookRepository.findByMajor(major);
            if (listBooks.isEmpty()) {
                throw new Exception(NOT_EXIST);
            } else {
                return BookCommons.showBooksInfo(listBooks, authorBookRepository, authorRepository, userBookRepository);
            }
        }
    }

    @Override
    public ArrayList<Book> getByLanguage(String language) throws Exception {
        if (StringUtils.isBlank(language)) {
            throw new Exception(DATA_NULL);
        } else {
            List<BookEntity> listBooks = bookRepository.findByLanguage(language);
            if (listBooks.isEmpty()) {
                throw new Exception(NOT_EXIST);
            } else {
                return BookCommons.showBooksInfo(listBooks, authorBookRepository, authorRepository, userBookRepository);
            }
        }
    }

}
