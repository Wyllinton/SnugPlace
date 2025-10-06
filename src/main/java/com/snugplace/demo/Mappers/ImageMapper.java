package com.snugplace.demo.Mappers;

import com.snugplace.demo.DTO.ImageDTO;
import com.snugplace.demo.Model.Accommodation;
import com.snugplace.demo.Model.Image;
import org.mapstruct.*;
import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ImageMapper {

    ImageDTO toImageDTO(Image image);

    @Mapping(target = "uploadedTime", expression = "java(java.time.LocalDate.now())")
    Image toEntity(ImageDTO imageDTO);

    List<ImageDTO> toDTOList(List<Image> images);

    //------------------------------------------------------------------
    @Named("mapAccommodation")
    default Accommodation mapAccommodation(Long idAccommodation) {
        if (idAccommodation == null) return null;
        Accommodation acc = new Accommodation();
        acc.setId(idAccommodation);
        return acc;
    }
}
