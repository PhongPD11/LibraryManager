package com.example.librarymanager.DTOs;

import com.example.librarymanager.Entity.AuthorEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Book{
    private Long id;
    private Long bookId;
    private String name;
    private Long amount;
    private List<AuthorEntity> author;
//    private List<String> type;
    private String type;
    private Double rated;
    private String imageUrl;
    private String major;
    private String language;
    private Long userRate;
    private String ddc;
    private String status;
    private Long borrowingPeriod;
    private String bookLocation;
    private Long publicationYear;
}
