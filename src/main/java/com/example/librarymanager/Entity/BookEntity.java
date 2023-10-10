package com.example.librarymanager.Entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Setter
@Getter
@Table(name = "book")
@Data
public class BookEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Long bookId;
    private Long amount;
    private Long  borrowingPeriod;
    private String bookLocation;
    private Double vote;
    private String imageUrl;
    private String major;
    private String language;
    private String type;
}
