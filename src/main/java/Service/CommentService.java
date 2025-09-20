package Service;

import DTO.Comment.AnswerCommentDTO;
import DTO.Comment.CreateCommentDTO;

public interface CommentService {

    void createComment(CreateCommentDTO createCommentDTO) throws Exception;

    AnswerCommentDTO answerCommentHost(String id) throws Exception;
}
