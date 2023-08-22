package com.example.librarymanager.DTOs;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookData {
    private Long id;
    private String name;
    private String author;
    private Long amount;
    private String type;
    private Long authorId;
}
