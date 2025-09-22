package DTO;

public record ImageDTO(
        Long id,
        Long idAccommodation,
        String url,
        boolean isMain
) {
}
