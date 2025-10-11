package com.snugplace.demo.Repository;

import com.snugplace.demo.Model.AnswerComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnswerCommentRepository extends JpaRepository<AnswerComment, Long> {
    boolean existsByCommentId(Long id);
}
