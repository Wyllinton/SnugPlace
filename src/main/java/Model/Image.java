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
@Table(name = "Image")
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false, length = 300)
    private String url;

    @Column(nullable = false)
    private LocalDate uploadedTime;

    @Column(nullable = false)
    private boolean isMainImage;

    @ManyToOne
    @JoinColumn(name = "accommodationId", nullable = false)
    private Accommodation accommodation;
}
