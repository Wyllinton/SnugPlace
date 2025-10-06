package com.snugplace.demo.DTO.Accommodation;

import com.snugplace.demo.Model.Enums.Service;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public record FilterAccommodationDTO(
        @NotNull @NotBlank String city,
        @FutureOrPresent LocalDate fechaCheckIn,
        @Future LocalDate fechaCheckOut,
        @PositiveOrZero
        @NotNull @NotBlank @Positive Double minPrice,
        @PositiveOrZero Double maxPrice,
        @Min(1) Integer guestsCount,
        Set<Service> services,
        @PositiveOrZero Integer page
) {
}
