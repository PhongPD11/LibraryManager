package com.example.librarymanager.DTOs;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ActiveCode {
    private String email;
    private Long activeCode;
}
