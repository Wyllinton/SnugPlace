package DTO.User;


public record UpdateProfileDTO(
        String name,
        String phoneNumber,
        String photoURL,
        String description
)
{
}
