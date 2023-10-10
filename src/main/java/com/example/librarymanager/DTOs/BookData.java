package com.example.librarymanager.DTOs;

import com.example.librarymanager.Entity.AuthorEntity;
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
//    private List<TypeEntity> type;
    private String type;
    private String language;
    private String bookLocation;
    private String imageUrl;
    private String major;
}
