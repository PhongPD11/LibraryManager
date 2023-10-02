package com.example.librarymanager.Commons;

import com.example.librarymanager.DTOs.Author;
import com.example.librarymanager.DTOs.Book;
import com.example.librarymanager.Entity.*;
import com.example.librarymanager.Repository.AuthorBookRepository;
import com.example.librarymanager.Repository.AuthorRepository;
import com.example.librarymanager.Repository.TypeBookRepository;
import com.example.librarymanager.Repository.TypeRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookCommons {

    public static String BOOK_NOT_FOUND = "bookNotFound";

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
    public static void saveTypeBook(List<TypeEntity> types, Long bookId, TypeBookRepository typeBookRepository, TypeRepository typeRepository) {
        for (TypeEntity type : types) {
            TypeBookEntity saveTypeBook = new TypeBookEntity();
            String typeName = type.getType();
            Optional<TypeEntity> typeExist = Optional.ofNullable(typeRepository.findByType(typeName));
            if (!typeExist.isPresent()) {
                break;
            } else {
                saveTypeBook.setTypeId(typeExist.get().getId());
                saveTypeBook.setBookId(bookId);
            }
            typeBookRepository.save(saveTypeBook);
        }
    }

    public static Book showBookInfo(BookEntity book, AuthorBookRepository authorBookRepository, AuthorRepository authorRepository, TypeBookRepository typeBookRepository, TypeRepository typeRepository){
        Book bookInfo = new Book();
        bookInfo.setName(book.getName());
        bookInfo.setAmount(book.getAmount());
        bookInfo.setBookId(book.getBookId());

        List<AuthorBookEntity> listAuthorBook = authorBookRepository.findByBookId(book.getBookId());
        ArrayList<String> listAuthor = new ArrayList<>();
        for (AuthorBookEntity authorBook : listAuthorBook) {
            listAuthor.add(authorRepository.findByAuthorId(authorBook.getAuthorId()).getAuthorName());
        }

        List<TypeBookEntity> listTypeBook = typeBookRepository.findByBookId(book.getBookId());
        ArrayList<String> listType = new ArrayList<>();
        for (TypeBookEntity typeBook : listTypeBook) {
            listType.add(typeRepository.findById(typeBook.getTypeId()).get().getType());
        }
        bookInfo.setAuthor(listAuthor);
        bookInfo.setType(listType);
        return bookInfo;
    }

    public static ArrayList<Book> showBooksInfo(List<BookEntity> listOfBook, AuthorBookRepository authorBookRepository, AuthorRepository authorRepository, TypeBookRepository typeBookRepository, TypeRepository typeRepository){
        ArrayList<Book> books = new ArrayList<>();
        for (BookEntity book : listOfBook) {
            books.add(showBookInfo(book, authorBookRepository, authorRepository, typeBookRepository, typeRepository));
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
