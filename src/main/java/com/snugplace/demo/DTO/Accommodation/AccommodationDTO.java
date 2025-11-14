package com.snugplace.demo.DTO.Accommodation;

import com.snugplace.demo.DTO.Comment.CommentDTO;
import com.snugplace.demo.DTO.User.HostDTO;
import com.snugplace.demo.Model.Enums.AccommodationStatus;
import com.snugplace.demo.Model.Enums.Service;
import com.snugplace.demo.Model.Image;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public record AccommodationDTO(
        @NotBlank @Size(max = 100) String title,
        @NotBlank @Size(max = 500) String description,
        @NotBlank String city,
        @NotBlank String address,
        double latitude,
        double longitude,
        @Positive double priceDay,
        @Min(1) int guestsCount,
        @DecimalMin("0.0") @DecimalMax("5.0") double averageRating,
        @NotNull @PastOrPresent LocalDate publicationDate,
        @NotNull @Size(min = 1) Set<Service> services,
        @NotNull AccommodationStatus status,
        @NotNull Image mainImage,
        @NotNull HostDTO host,
        List<CommentDTO> comments
) {}
