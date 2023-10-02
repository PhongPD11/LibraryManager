package com.example.librarymanager.DTOs;

import com.example.librarymanager.Entity.AuthorEntity;
import com.example.librarymanager.Entity.TypeEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BookData {
    private Long id;
    private Long bookId;
    private String name;
    private Long amount;
    private Long borrowingPeriod;
    private List<AuthorEntity> author;
    private List<TypeEntity> type;
    private String bookLocation;
}
