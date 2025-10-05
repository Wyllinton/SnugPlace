package com.snugplace.demo.Service.Implementation;

import com.snugplace.demo.DTO.Comment.AnswerCommentDTO;
import com.snugplace.demo.DTO.Comment.CreateCommentDTO;
import com.snugplace.demo.Service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    @Override
    public void createComment(CreateCommentDTO createCommentDTO) throws Exception {

    }

    @Override
    public AnswerCommentDTO answerCommentHost(Long id) throws Exception {
        return null;
    }
}
