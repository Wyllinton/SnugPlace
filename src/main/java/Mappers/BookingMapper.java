package Mappers;

import DTO.Accommodation.AccommodationDTO;
import DTO.Accommodation.CreateAccommodationDTO;
import DTO.Booking.BookingDTO;
import DTO.Booking.CreateBookingDTO;
import Model.Accommodation;
import Model.Booking;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,  unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BookingMapper {

    //@Mapping(target = "id", expression = "java(java.util.UUID.randomUUID().toString())")
    //@Mapping(target = "status", constant = "PENDING")
    //@Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    Booking toEntity(CreateBookingDTO bookingDTO);
    BookingDTO toBookingDTO(Booking booking);

    List<BookingDTO> toDTOList(List<Booking> bookings);
    List<Booking> toEntityList(List<BookingDTO> bookingsDTO);
}
