package com.snugplace.demo.Mappers;

import com.snugplace.demo.DTO.User.*;
import com.snugplace.demo.Model.Enums.Role;
import com.snugplace.demo.Model.User;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "Role", expression = "java((userDTO.role() != null && userDTO.role() != com.snugplace.demo.Model.Enums.Role.USER) ? userDTO.role() : com.snugplace.demo.Model.Enums.Role.GUEST)")
        // El status se mapea automáticamente desde el DTO
    User toEntity(CreateUserDTO userDTO);

    // Asegurar que profilePhoto se mapee a photoUrl
    @Mapping(source = "profilePhoto", target = "photoUrl")
    UserDTO toUserDTO(User user);

    @Mapping(target = "status", constant = "ACTIVE")
    void updateFromDTO(UpdateProfileDTO request, @MappingTarget User user);

    List<UserDTO> toDTOList(List<User> users);

    HostDTO toHostDTO(User user);

    UserResponseDTO toUserResponseDTO(User user);
}