package com.snugplace.demo.Service.Implementation;

import com.snugplace.demo.DTO.Comment.AnswerCommentDTO;
import com.snugplace.demo.DTO.Comment.CreateCommentDTO;
import com.snugplace.demo.Mappers.CommentMapper;
import com.snugplace.demo.Model.Comment;
import com.snugplace.demo.Repository.CommentRepository;
import com.snugplace.demo.Service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;

    @Override
    public void createComment(CreateCommentDTO createCommentDTO) throws Exception {
        Comment comment = commentMapper.toEntity(createCommentDTO);
        commentRepository.save(comment);
    }

    @Override
    public AnswerCommentDTO answerCommentHost(Long id) throws Exception {
        return null;
    }
}
