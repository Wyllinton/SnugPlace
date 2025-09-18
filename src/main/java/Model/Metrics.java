package Model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder

public class Metrics {

    private String idAccommodation;
    private long totalReservations;
    private double averageRating;
    private double totalMount;
}
