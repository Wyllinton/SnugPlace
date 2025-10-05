package com.snugplace.demo.Service;

import com.snugplace.demo.DTO.Booking.*;

import java.util.List;

public interface BookingService {

    void createBooking(CreateBookingDTO createBookingDTO) throws Exception;

    List<BookingDTO> searchFilteredBooking(FilteredBookingDTO filteredBookingDTO) throws Exception;

    BookingDetailDTO bookingDetail(Long id) throws Exception;

    void cancelBooking(Long id, String reason) throws Exception;

    List<BookingDTO> searchFilteredBookingsHost(Long id) throws Exception;

    List<BookingDTO> searchFilteredBookingsUser (Long id) throws Exception;
}
