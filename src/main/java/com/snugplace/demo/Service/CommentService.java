package com.snugplace.demo.Service;

import com.snugplace.demo.DTO.Comment.AnswerCommentDTO;
import com.snugplace.demo.DTO.Comment.CreateCommentDTO;

public interface CommentService {

    void createComment(CreateCommentDTO createCommentDTO) throws Exception;

    AnswerCommentDTO answerCommentHost(Long id) throws Exception;
}
