package com.snugplace.demo.DTO.Accommodation;

import com.snugplace.demo.DTO.ImageDTO;
import com.snugplace.demo.Model.Enums.Service;
import jakarta.validation.constraints.*;
import java.util.Set;

public record UpdateAccommodationDTO(
        @NotBlank @Size(max = 100) String title,
        @NotBlank @Size(max = 500) String description,
        @Positive double priceDay,
        @Min(1) int guestsCount,
        @NotNull @Size(min = 1) Set<Service> services,
        @NotNull Set<ImageDTO> images
) {
}
