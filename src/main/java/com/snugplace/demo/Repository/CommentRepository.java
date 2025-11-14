package com.snugplace.demo.Repository;

import com.snugplace.demo.Model.Comment;
import com.snugplace.demo.Model.Enums.AccommodationStatus;
import com.snugplace.demo.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByAccommodationId(Long id);

    boolean existsByUserIdAndAccommodation_Id(Long id, Long accommodationId);

}
