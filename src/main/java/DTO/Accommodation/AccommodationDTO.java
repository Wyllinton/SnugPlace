package DTO.Accommodation;

import DTO.User.HostDTO;
import Model.Image;

import java.time.LocalDate;
import java.util.List;

public record AccommodationDTO(
        Long id,
        String title,
        String description,
        String city,
        String address,
        double priceDay,
        int guestsCount,
        double averageRating,
        LocalDate publicationDate,
        List<String> services,
        Image mainImage,
        HostDTO host
) {

}

