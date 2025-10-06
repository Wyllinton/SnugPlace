package com.snugplace.demo.DTO.Accommodation;

import com.snugplace.demo.DTO.Comment.CommentDTO;
import com.snugplace.demo.DTO.User.HostDTO;
import com.snugplace.demo.Model.Enums.AccommodationStatus;
import com.snugplace.demo.Model.Enums.Service;
import com.snugplace.demo.Model.Image;

import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.util.List;

public record CreateAccommodationDTO(
        @NotNull HostDTO host,
        @NotBlank @Size(max = 100) String title,
        @NotBlank @Size(max = 500) String description,
        @NotBlank String city,
        @NotBlank String address,
        @DecimalMin(value = "-90.0") @DecimalMax(value = "90.0") double latitude,
        @DecimalMin(value = "-180.0") @DecimalMax(value = "180.0") double longitude,
        @Positive double priceDay,
        @Min(1) int guestsCount,
        @DecimalMin("0.0") @DecimalMax("5.0") double averageRating,
        @NotNull @PastOrPresent LocalDate publicationDate,
        @NotNull AccommodationStatus status,
        @NotNull @Size(min = 1) List<Service> services,
        @NotNull List<Image> images,
        List<CommentDTO> comments
) {

}
