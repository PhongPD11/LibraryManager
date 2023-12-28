package com.example.librarymanager.DTOs;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BorrowBook {
    private Long uid;
    private Long bookId;
    private String address;
    private Boolean isDelivery;
    private String phoneNumber;
}
