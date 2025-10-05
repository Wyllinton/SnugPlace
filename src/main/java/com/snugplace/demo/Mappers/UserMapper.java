package com.snugplace.demo.Mappers;


import com.snugplace.demo.DTO.User.*;
import com.snugplace.demo.Model.Enums.Role;
import com.snugplace.demo.Model.User;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", constant = "ACTIVE")
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    User toEntity(CreateUserDTO userDTO);

    UserDTO toUserDTO(User user);

    @Mapping(target = "status", constant = "ACTIVE")
    void updateFromDTO(UpdateProfileDTO request, @MappingTarget User user);

    List<UserDTO> toDTOList(List<User> users);

    HostDTO toHostDTO(User user);

    UserResponseDTO toUserResponseDTO(User user);
}
