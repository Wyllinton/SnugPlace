package com.snugplace.demo.Mappers;

import com.snugplace.demo.DTO.Accommodation.*;
import com.snugplace.demo.Model.Accommodation;
import org.mapstruct.*;
import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {UserMapper.class, CommentMapper.class, ImageMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AccommodationMapper {

    @Mapping(source = "user", target = "host")
    @Mapping(source = "comments", target = "comments")
    @Mapping(target = "mainImage", expression = "java(accommodation.getImages() != null && !accommodation.getImages().isEmpty() ? accommodation.getImages().get(0) : null)")
    @Mapping(target = "averageRating", expression = "java(calculateAverageRating(accommodation))")
    AccommodationDTO toAccommodationDTO(Accommodation accommodation);

    List<AccommodationDTO> toDTOList(List<Accommodation> accommodations);

    @Mapping(target = "id", ignore = true) // DB genera
    @Mapping(source = "host.id", target = "user.id")
    @Mapping(source = "images", target = "images")
    @Mapping(source = "comments", target = "comments")
    Accommodation toEntity(CreateAccommodationDTO dto);

    default double calculateAverageRating(Accommodation accommodation) {
        if (accommodation.getComments() == null || accommodation.getComments().isEmpty()) {
            return 0.0;
        }
        return accommodation.getComments().stream()
                .mapToDouble(c -> c.getRating())
                .average()
                .orElse(0.0);
    }
}
