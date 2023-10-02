package com.example.librarymanager.Entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Data
@Table(name = "user_book")
public class UserBookEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long bookId;
    private Long uid;
    private LocalDateTime createAt;
    private LocalDateTime createdAt;
    private LocalDateTime expireAt;
    private LocalDateTime returnedAt;
    private String status;
}
