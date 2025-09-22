package Mappers;

import DTO.Accommodation.AccommodationDTO;
import DTO.Accommodation.CreateAccommodationDTO;
import Model.Accommodation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {UserMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)

public interface AccommodationMapper {

    //@Mapping(target = "id", ignore = true)
    //@Mapping(target = "status", constant = "ACTIVE")
    //@Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    Accommodation toEntity(CreateAccommodationDTO accommodationDTO);


    @Mapping(target = "averageRating", ignore = true) // Se calcula en el service
    //@Mapping(target = "commentsTotal", ignore = true) // Se calcula en el service
    AccommodationDTO toAccommodationDTO(Accommodation accommodation);

    List<AccommodationDTO> toDTOList(List<Accommodation> accommodations);
    List<Accommodation> toEntityList(List<AccommodationDTO> accommodationsDTO);
}
