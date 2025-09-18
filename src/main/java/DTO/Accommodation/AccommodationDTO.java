package DTO.Accommodation;

import Model.Image;

import java.time.LocalDate;
import java.util.List;

public record AccommodationDTO(
        String id,
        String title,
        String description,
        String city,
        String address,
        double priceDay,
        int guestsCount,
        double averageRating,
        LocalDate publicationDate,
        List<String> services,
        Image mainImage
) {

}

