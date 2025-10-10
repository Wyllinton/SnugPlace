package com.snugplace.demo.DTO.Accommodation;

import com.snugplace.demo.Model.Enums.Service;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public record FilterAccommodationDTO(
        String city,
        @FutureOrPresent LocalDate checkIn,
        @Future LocalDate checkOut,
        @PositiveOrZero
        @Positive Double minPrice,
        @PositiveOrZero Double maxPrice,
        @Min(1) Integer guestsCount,
        Set<Service> services,
        @PositiveOrZero Integer page
) {
}
