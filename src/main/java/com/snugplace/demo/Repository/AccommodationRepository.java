package com.snugplace.demo.Repository;

import com.snugplace.demo.Model.Accommodation;
import com.snugplace.demo.Model.Enums.AccommodationStatus;
import com.snugplace.demo.Model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccommodationRepository extends JpaRepository<Accommodation, Long> {
    @EntityGraph(attributePaths = {"comments", "images", "user"})
    Optional<Accommodation> findWithDetailsById(Long id);

    Page<Accommodation> findByUserAndStatus(User user, AccommodationStatus status, Pageable pageable);

    List<Accommodation> findByUser_Id(Long userId);



    // 🔥 SOLUCIÓN COMPLETA: Incluir TODAS las relaciones lazy
    @Query("SELECT DISTINCT a FROM Accommodation a " +
            "LEFT JOIN FETCH a.images " +
            "LEFT JOIN FETCH a.comments " +
            "LEFT JOIN FETCH a.services " + // 🔥 AGREGAR SERVICES
            "LEFT JOIN FETCH a.user " +     // 🔥 AGREGAR USER
            "WHERE a.status = 'ACTIVE'")
    List<Accommodation> findAllWithImagesAndComments();

    // Método con filtro de precio
    @Query("SELECT DISTINCT a FROM Accommodation a " +
            "LEFT JOIN FETCH a.images " +
            "LEFT JOIN FETCH a.comments " +
            "LEFT JOIN FETCH a.services " + // 🔥 AGREGAR SERVICES
            "LEFT JOIN FETCH a.user " +     // 🔥 AGREGAR USER
            "WHERE a.status = 'ACTIVE' AND a.priceDay <= :maxPrice")
    List<Accommodation> findAllWithImagesAndCommentsByMaxPrice(Double maxPrice);
}
