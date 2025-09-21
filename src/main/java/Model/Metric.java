package Model;

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
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String idAccommodation;

    @Column(nullable = false)
    private long totalReservations;

    @Column(nullable = false)
    private double averageRating;

    @Column(nullable = false)
    private double totalMount;
}
