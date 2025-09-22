package Model;

import Model.Enums.AccommodationStatus;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Accommodation")
public class Accommodation {

    @Id
    //@GeneratedValue(strategy = GenerationType.UUID)
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
    private int guetsCount;

    @Column(nullable = false)
    private LocalDate publicationDate;

    @Enumerated(EnumType.STRING)
    private AccommodationStatus status;

    @ElementCollection
    private List<String> services;

    @OneToMany(mappedBy = "accommodation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> images;

    @OneToMany(mappedBy = "accommodation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Booking> bookings;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user;
}
