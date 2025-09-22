package Mappers;

import DTO.Booking.BookingDTO;
import DTO.Booking.CreateBookingDTO;
import DTO.Comment.CommentDTO;
import Model.Booking;
import Model.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)

public interface CommentMapper {

    Comment toEntity(CommentDTO commentDTO);
    CommentDTO toCommentDTO(Comment comment);
}
