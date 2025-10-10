package com.snugplace.demo.Mappers;

import com.snugplace.demo.DTO.Comment.AnswerCommentDTO;
import com.snugplace.demo.DTO.Comment.CommentDTO;
import com.snugplace.demo.DTO.Comment.CreateCommentDTO;
import com.snugplace.demo.Model.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,  unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CommentMapper {

    @Mapping(target = "idBooking", source = "booking.id")
    @Mapping(target = "user", source = "user")
    CommentDTO toCommentDTO(Comment comment);

    @Mapping(target = "booking.id", source = "idBooking")
    @Mapping(target = "accommodation", ignore = true) // se setea después en el service si hace falta
    Comment toEntity(CreateCommentDTO dto);

    List<CommentDTO> toDTOList(List<Comment> comments);

    AnswerCommentDTO toAnswerCommentDTO(Comment comment);
}
