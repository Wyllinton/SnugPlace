package com.snugplace.demo.Mappers;

import com.snugplace.demo.DTO.Accommodation.*;
import com.snugplace.demo.Model.Accommodation;
import com.snugplace.demo.Model.Image;
import org.mapstruct.*;
import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        uses = {UserMapper.class, CommentMapper.class},
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AccommodationMapper {

    @Mapping(source = "user", target = "host")
    @Mapping(source = "comments", target = "comments")
    @Mapping(source = "guestsCount", target = "guestsCount")
    // ✅ CORREGIDO: Ahora mapea al campo mainImage que es String
    @Mapping(source = "mainImage", target = "mainImage")
    @Mapping(target = "averageRating", expression = "java(calculateAverageRating(accommodation))")
    AccommodationDTO toAccommodationDTO(Accommodation accommodation);

    List<AccommodationDTO> toDTOList(List<Accommodation> accommodations);

    // ✅ PARA create - ignorar todo lo relacionado con imágenes
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "images", ignore = true)
    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "publicationDate", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "mainImage", ignore = true) // Se establecerá en el servicio
    @Mapping(source = "host.id", target = "user.id")
    @Mapping(source = "services", target = "services")
    Accommodation toEntity(CreateAccommodationDTO dto); //Se COMPLETÓ EL PROYECTO

    default double calculateAverageRating(Accommodation accommodation) {
        if (accommodation.getComments() == null || accommodation.getComments().isEmpty()) {
            return 0.0;
        }
        return accommodation.getComments().stream()
                .mapToDouble(c -> c.getRating())
                .average()
                .orElse(0.0);
    }

    // ❌ ELIMINAR este método ya que no se necesita
    // El campo mainImage en Accommodation ahora es String, no Image
}