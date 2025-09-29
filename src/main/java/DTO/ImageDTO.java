package DTO;

import jakarta.validation.constraints.NotNull;

public record ImageDTO(
        @NotNull Long id,
        @NotNull Long idAccommodation,
        @NotNull String url,
        boolean isMain
) {
}
