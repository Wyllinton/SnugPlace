package Model;


import Model.Enums.AccommodationStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
public class Accommodation {
    private String id;
    private String title;
    private String description;
    private String city;
    private String address;
    private double latitude;
    private double longitude;
    private double priceDay;
    private int guetsCount;
    private LocalDate publicationDate;
    private AccommodationStatus status;
    private List<String> services;
    private List<Image> images;
    private List<Comment> comments;
    private List<Accommodation> accommodations;
    private User user;

}
