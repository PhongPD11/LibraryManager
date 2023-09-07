package com.example.librarymanager.Commons;

import com.example.librarymanager.DTOs.Author;
import com.example.librarymanager.DTOs.Book;
import com.example.librarymanager.Entity.AuthorBookEntity;
import com.example.librarymanager.Entity.AuthorEntity;
import com.example.librarymanager.Entity.BookEntity;
import com.example.librarymanager.Repository.AuthorBookRepository;
import com.example.librarymanager.Repository.AuthorRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookCommons {

    public static void saveAuthorBook(List<AuthorEntity> authors, Long bookId, AuthorBookRepository authorBookRepository, AuthorRepository authorRepository) {
        for (AuthorEntity author : authors) {
            AuthorEntity saveAuthor = new AuthorEntity();
            AuthorBookEntity saveAuthorBook = new AuthorBookEntity();
            Long authorId = author.getAuthorId();
            String authorName = author.getAuthorName();
            Optional<AuthorEntity> authorExist = Optional.ofNullable(authorRepository.findByAuthorId(authorId));
            if (!authorExist.isPresent()) {
                saveAuthor.setAuthorId(authorId);
                saveAuthor.setAuthorName(authorName);
                saveAuthor = authorRepository.save(saveAuthor);
                author.setId(saveAuthor.getId());
            } else {
                author.setId(authorExist.get().getId());
            }
            saveAuthorBook.setBookId(bookId);
            saveAuthorBook.setAuthorId(authorId);
            authorBookRepository.save(saveAuthorBook);
        }
    }

    public static void updateAuthorBook(List<AuthorBookEntity> oldAuthorBook, List<AuthorEntity> authors, Long bookId, AuthorBookRepository authorBookRepository, AuthorRepository authorRepository){
        authorBookRepository.deleteAll(oldAuthorBook);
        for (AuthorEntity author : authors) {
            AuthorEntity saveAuthor = new AuthorEntity();
            AuthorBookEntity saveAuthorBook = new AuthorBookEntity();
            Long authorId = author.getAuthorId();
            String authorName = author.getAuthorName();
            Optional<AuthorEntity> authorExist = Optional.ofNullable(authorRepository.findByAuthorId(authorId));
            if (!authorExist.isPresent()) {
                saveAuthor.setAuthorId(authorId);
                saveAuthor.setAuthorName(authorName);
                saveAuthor = authorRepository.save(saveAuthor);
                author.setId(saveAuthor.getId());
            } else {
                authorExist.get().setAuthorName(authorName);
                author.setId(authorExist.get().getId());
            }
            saveAuthorBook.setBookId(bookId);
            saveAuthorBook.setAuthorId(authorId);
            authorBookRepository.save(saveAuthorBook);
        }
    }

    public static Book showBookInfo(BookEntity book, AuthorBookRepository authorBookRepository, AuthorRepository authorRepository){
        Book bookInfo = new Book();
        bookInfo.setName(book.getName());
        bookInfo.setType(book.getType());
        bookInfo.setAmount(book.getAmount());
        bookInfo.setBookId(book.getBookId());
        List<AuthorBookEntity> listAuthorBook = authorBookRepository.findByBookId(book.getBookId());
        ArrayList<String> listAuthor = new ArrayList<>();
        for (AuthorBookEntity authorBook : listAuthorBook) {
            listAuthor.add(authorRepository.findByAuthorId(authorBook.getAuthorId()).getAuthorName());
        }
        bookInfo.setAuthor(listAuthor);
        return bookInfo;
    }

    public static ArrayList<Book> showBooksInfo(List<BookEntity> listOfBook, AuthorBookRepository authorBookRepository, AuthorRepository authorRepository){
        ArrayList<Book> books = new ArrayList<>();
        for (BookEntity book : listOfBook) {
            books.add(showBookInfo(book, authorBookRepository, authorRepository));
        }
        return books;
    }

    public static ArrayList<Author> showAuthors(List<AuthorEntity> listOfAuthor){
        ArrayList<Author> authors = new ArrayList<>();
        for (AuthorEntity author : listOfAuthor) {
            Author authorInfo = new Author();
            authorInfo.setAuthorName(author.getAuthorName());
            authorInfo.setAuthorId(author.getAuthorId());
            authors.add(authorInfo);
        }
        return authors;
    }
}
