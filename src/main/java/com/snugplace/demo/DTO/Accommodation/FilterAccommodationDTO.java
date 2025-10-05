package com.snugplace.demo.DTO.Accommodation;

import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.List;

public record FilterAccommodationDTO(
        @NotNull @NotBlank String city,
        @FutureOrPresent LocalDate fechaCheckIn,
        @Future LocalDate fechaCheckOut,
        @PositiveOrZero
        @NotNull @NotBlank @Positive Double minPrice,
        @PositiveOrZero Double maxPrice,
        @Min(1) Integer guestsCount,
        List<String> services,
        @PositiveOrZero Integer page
) {
}
