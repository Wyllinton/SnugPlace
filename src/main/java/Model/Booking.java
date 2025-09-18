package Model;

import Model.Enums.BookingStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder

public class Booking {

    private String id;
    private LocalDate dateCheckIn;
    private LocalDate dateCheckOut;
    private int guestsCount;
    private BookingStatus status;
    private double price;
    Accommodation accommodation;
    List<Comment> comments;
    User user;
}
