package com.snugplace.demo.Service.Implementation;

import com.snugplace.demo.Model.Booking;
import com.snugplace.demo.Model.Enums.BookingStatus;
import com.snugplace.demo.Repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingStatusServiceImpl {
    private final BookingRepository bookingRepository;

    // Se ejecuta cada hora
    @Scheduled(fixedRate = 60 * 60 * 1000)
    public void updateBookingStatuses() {
        LocalDate today = LocalDate.now();

        // De PENDING a CONFIRMED
        List<Booking> pendingBookings = bookingRepository.findByStatus(BookingStatus.PENDING);
        for (Booking b : pendingBookings) {
            if (!b.getDateCheckIn().isAfter(today)) {
                b.setStatus(BookingStatus.CONFIRMED);
                bookingRepository.save(b);
            }
        }

        // De CONFIRMED a COMPLETED
        List<Booking> confirmedBookings = bookingRepository.findByStatus(BookingStatus.CONFIRMED);
        for (Booking b : confirmedBookings) {
            if (!b.getDateCheckOut().isAfter(today)) {
                b.setStatus(BookingStatus.COMPLETED);
                bookingRepository.save(b);
            }
        }
    }
}
