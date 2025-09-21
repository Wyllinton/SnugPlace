package DTO.User;


public record UpdateProfileDTO(
        String id,
        String name,
        String phoneNumber,
        String photoURL,
        String description
)
{
}
