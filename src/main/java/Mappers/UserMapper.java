package Mappers;


import DTO.User.CreateUserDTO;
import DTO.User.UpdateProfileDTO;
import DTO.User.UserDTO;
import Model.Enums.Role;
import Model.User;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    //@Mapping(target = "id", ignore = true)
    //@Mapping(target = "status", constant = "ACTIVE")
    //@Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    //@Mapping(source = "role", target = "role", qualifiedByName = "stringToRole")
    User toEntity(CreateUserDTO userDTO);

    //@Mapping(source = "role", target = "role", qualifiedByName = "roleToString")
    UserDTO toUserDTO(User user);

    //@Mapping(target = "id", ignore = true)
    //@Mapping(target = "email", ignore = true)
    //@Mapping(target = "password", ignore = true)
    //@Mapping(target = "role", ignore = true)
    //@Mapping(target = "birthDate", ignore = true)
    //@Mapping(target = "createdAt", ignore = true)
    //@Mapping(target = "status", constant = "ACTIVE")
    void updateFromDTO(UpdateProfileDTO request, @MappingTarget User user);

    List<UserDTO> toDTOList(List<User> users);

    @Named("roleToString")
    default String rolToString(Role rol) {
        return rol != null ? rol.name() : null;
    }

    @Named("stringToRole")
    default Role stringToRol(String rol) {
        return rol != null ? Role.valueOf(rol) : null;
    }
}
