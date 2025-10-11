package com.snugplace.demo.Repository;

import com.snugplace.demo.Model.Booking;
import com.snugplace.demo.Model.Enums.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("SELECT b FROM Booking b WHERE b.accommodation.id = :accommodationId " +
            "AND b.status <> 'CANCELED' " +
            "AND (:checkIn < b.dateCheckOut AND :checkOut > b.dateCheckIn)")
    List<Booking> findOverlappingBookings(@Param("accommodationId") Long accommodationId,
                                          @Param("checkIn") LocalDate checkIn,
                                          @Param("checkOut") LocalDate checkOut);

    List<Booking> findByStatus(BookingStatus status);
}
