package Model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder

public class Comment {

    private Booking booking;
    private User user;
    private String id;
    private double rating;
    private String comment;
    private LocalDate date;


}
