package Service;

import DTO.*;

public interface CommentService {

    void createComment(CreateCommentDTO createCommentDTO) throws Exception;

    AnswerCommentDTO answerCommentHost(String id) throws Exception;
}
