package com.snugplace.demo.Mappers;

import com.snugplace.demo.DTO.Booking.*;
import com.snugplace.demo.Model.Booking;
import org.mapstruct.*;
import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {UserMapper.class, CommentMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BookingMapper {

    @Mapping(source = "accommodation.id", target = "idAccommodation")
    @Mapping(source = "user", target = "user")
    @Mapping(source = "comments", target = "comments")
    @Mapping(target = "createDate", expression = "java(java.time.LocalDate.now())")
    BookingDTO toBookingDTO(Booking booking);

    @Mapping(source = "accommodation.id", target = "idAccommodation")
    @Mapping(source = "user", target = "user")
    @Mapping(source = "comments", target = "comments")
    @Mapping(target = "createDate", expression = "java(java.time.LocalDate.now())")
    BookingDetailDTO toBookingDetailDTO(Booking booking);

    List<BookingDTO> toBookingDTOList(List<Booking> bookings);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "accommodation.id", source = "idAccommodation")
    @Mapping(target = "user.id", source = "idUser")
    @Mapping(source = "dateCheckIn", target = "dateCheckIn")
    @Mapping(source = "dateCheckOut", target = "dateCheckOut")
    @Mapping(source = "guestsCount", target = "guestsCount")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "price", target = "price")
    Booking toEntity(CreateBookingDTO dto);
}
