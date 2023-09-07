package com.example.librarymanager.DTOs;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Book{
    private Long bookId;
    private String name;
    private String type;
    private Long amount;
    private List<String> author;
}
