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
    @Query("SELECT b FROM Booking b WHERE b.accommodation.user.id = :userId")
    List<Booking> findBookingsByAccommodationUserId(@Param("userId") Long userId);
    @Query("SELECT b FROM Booking b WHERE b.user.id = :userId")
    List<Booking> findBookingsByUserId(@Param("userId") Long userId);
    List<Booking> findByAccommodationIdAndDateCheckInBetween(Long id, LocalDate firstDate, LocalDate LastDate);
    @Query("SELECT b FROM Booking b WHERE b.dateCheckIn = :checkInDate AND b.status = :status")
    List<Booking> findByCheckInDateAndStatus(@Param("checkInDate") LocalDate checkInDate,
                                             @Param("status") BookingStatus status);



}
