package DTO.Accommodation;

import DTO.Comment.CommentDTO;
import Model.Enums.AccommodationStatus;
import Model.Image;

import java.time.LocalDate;
import java.util.List;

public record CreateAccommodationDTO(
        String id,
        String title,
        String description,
        String city,
        String address,
        double latitude,
        double longitude,
        double priceDay,
        int guestsCount,
        double averageRating,
        LocalDate publicationDate,
        AccommodationStatus status,
        List<String> services,
        List<Image> images,
        List<CommentDTO> comments
) {
}
