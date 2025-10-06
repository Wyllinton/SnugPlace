package com.snugplace.demo.Repository;

import com.snugplace.demo.Model.Accommodation;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccommodationRepository extends JpaRepository<Accommodation, Long> {
    @EntityGraph(attributePaths = {"comments", "images", "user"})
    Optional<Accommodation> findWithDetailsById(Long id);
}
