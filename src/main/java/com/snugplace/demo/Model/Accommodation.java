package com.snugplace.demo.Model;

import com.snugplace.demo.Model.Enums.AccommodationStatus;
import com.snugplace.demo.Model.Enums.Service;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Accommodation")
public class Accommodation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 150)
    private String title;

    @Column(nullable = false, length = 500)
    private String description;

    @Column(nullable = false, length = 100)
    private String city;

    @Column(nullable = false, length = 200)
    private String address;

    private double latitude;
    private double longitude;

    @Column(nullable = false)
    private double priceDay;

    @Column(nullable = false)
    private int guestsCount;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDate publicationDate;

    @Enumerated(EnumType.STRING)
    private AccommodationStatus status;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    private Set<Service> services;

    // Use Set to avoid Hibernate MultipleBagFetchException when fetching multiple collections
    @OneToMany(mappedBy = "accommodationId", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Image> images;

    @OneToMany(mappedBy = "accommodationId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user;
}
