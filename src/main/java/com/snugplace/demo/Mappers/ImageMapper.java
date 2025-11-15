package com.snugplace.demo.Mappers;

import com.snugplace.demo.DTO.ImageDTO;
import com.snugplace.demo.Model.Image;
import org.mapstruct.*;
import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ImageMapper {

    // ✅ Para convertir de Entity a DTO (esto está bien)
    ImageDTO toImageDTO(Image image);

    // ✅ CORREGIDO: Ignorar accommodationId completamente en el mapeo
    @Mapping(target = "accommodationId", ignore = true)
    @Mapping(target = "uploadedTime", expression = "java(java.time.LocalDate.now())")
    Image toEntity(ImageDTO imageDTO);

    List<ImageDTO> toDTOList(List<Image> images);

    // ❌ ELIMINAR estos métodos que pueden causar conflictos
    // @Named("mapAccommodation")
    // default Accommodation mapAccommodation(Long idAccommodation) {
    //     if (idAccommodation == null) return null;
    //     Accommodation acc = new Accommodation();
    //     acc.setId(idAccommodation);
    //     return acc;
    // }
}