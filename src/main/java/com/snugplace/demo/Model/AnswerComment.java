package com.snugplace.demo.Model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "AnswerComment")
public class AnswerComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "commentId", nullable = false)
    private Comment comment;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @Column(nullable = false, length = 500)
    private String answer;

    @Column(nullable = false)
    private LocalDate date;
}
