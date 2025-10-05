package com.snugplace.demo.Model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Metric")
public class Metric {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long idAccommodation;

    @Column(nullable = false)
    private long totalBookings;

    @Column(nullable = false)
    private double averageRating;

    @Column(nullable = false)
    private double totalMount;
}
