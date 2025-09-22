package Mappers;

import DTO.Accommodation.AccommodationDTO;
import DTO.Accommodation.CreateAccommodationDTO;
import Model.Accommodation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {UserMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)

public interface AccommodationMapper {

    @Mapping(target = "id", expression = "java(java.util.UUID.randomUUID().toString())")
    @Mapping(target = "status", constant = "ACTIVE")
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    Accommodation toEntity(CreateAccommodationDTO accommodationDTO);

    @Mapping(source = "anfitrion.id", target = "anfitrionId")
    @Mapping(source = "anfitrion.nombre", target = "anfitrionNombre")
    @Mapping(source = "estado", target = "estado", qualifiedByName = "estadoToString")
    @Mapping(source = "serviciosAlojamiento", target = "servicios", qualifiedByName = "serviciosToStringList")
    @Mapping(source = "imagenes", target = "imagenes", qualifiedByName = "imagenesAUrlList")
    @Mapping(target = "promedioCalificaciones", ignore = true) // Se calcula en el service
    @Mapping(target = "totalComentarios", ignore = true) // Se calcula en el service
    AccommodationDTO toAccommodationDTO(Accommodation accommodation);
}
