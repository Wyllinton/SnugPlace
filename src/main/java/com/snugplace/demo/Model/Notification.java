package com.snugplace.demo.Model;

import com.snugplace.demo.Model.Enums.TypeNotification;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Notification")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, length = 500)
    private String message;

    @Enumerated(EnumType.STRING)
    private TypeNotification typeNotification;

    @Column(nullable = false)
    private LocalDateTime date;

    @Column(name = "is_read",nullable = false)
    private boolean read;

    @ManyToOne
    @JoinColumn(name = "receiverId", nullable = false)
    private User receiver;
}
