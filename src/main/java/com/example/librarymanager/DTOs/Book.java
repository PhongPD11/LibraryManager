package com.example.librarymanager.DTOs;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Book{
    private Long bookId;
    private String name;
    private Long amount;
    private List<String> author;
    private List<String> type;
    private Double vote;
    private String imageUrl;
}
