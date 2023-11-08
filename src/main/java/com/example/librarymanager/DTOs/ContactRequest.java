package com.example.librarymanager.DTOs;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContactRequest {
    private Long enquiryId;
    private String content;
    private String title;
    private Long uid;
}


