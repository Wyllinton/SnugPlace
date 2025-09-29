package Model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Comment")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private double rating;

    @Column(nullable = false, length = 500)
    private String comment;

    @Column(nullable = false)
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "bookingId", nullable = false)
    private Booking booking;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user;
}
