package Mappers;

import DTO.Booking.BookingDTO;
import DTO.Booking.CreateBookingDTO;
import DTO.Comment.CommentDTO;
import DTO.Comment.CreateCommentDTO;
import Model.Booking;
import Model.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,  unmappedTargetPolicy = ReportingPolicy.IGNORE)

public interface CommentMapper {

    CommentDTO toCommentDTO(Comment comment);
    Comment toEntity(CreateCommentDTO commentDTO);

    List<CommentDTO> toDTOList(List<Comment> comments);
    List<Comment> toEntityList(List<CommentDTO> commentsDTO);
}
