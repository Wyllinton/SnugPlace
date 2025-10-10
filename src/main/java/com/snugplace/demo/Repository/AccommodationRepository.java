package com.snugplace.demo.Repository;

import com.snugplace.demo.Model.Accommodation;
import com.snugplace.demo.Model.Enums.AccommodationStatus;
import com.snugplace.demo.Model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface AccommodationRepository extends JpaRepository<Accommodation, Long> {
    @EntityGraph(attributePaths = {"comments", "images", "user"})
    Optional<Accommodation> findWithDetailsById(Long id);
    Page<Accommodation> findByUserAndStatus(User user, AccommodationStatus status, Pageable pageable);

}
