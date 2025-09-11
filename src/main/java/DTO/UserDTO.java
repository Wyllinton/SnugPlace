package DTO;


import Model.Enums.Role;

public record UserDTO(
        String id,
        String name,
        String email,
        String photoUrl,
        Role role
)
{
}
