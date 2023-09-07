package com.example.librarymanager.DTOs;

import com.example.librarymanager.Entity.AuthorEntity;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class BookData {
    private Long id;
    private Long bookId;
    private String name;
    private Long amount;
    private String type;
    private List<AuthorEntity> author;
}
