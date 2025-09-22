package Mappers;

import DTO.ImageDTO;
import Model.Image;

public interface ImageMapper {

    ImageDTO toDTO(Image imagen);
    Image toEntity(ImageDTO imagenDTO);
}
