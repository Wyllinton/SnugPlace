package com.snugplace.demo.Service;

import com.snugplace.demo.DTO.Booking.*;

import java.util.List;

public interface BookingService {

    void createBooking(CreateBookingDTO createBookingDTO) throws Exception;

    List<BookingDTO> searchFilteredBooking(FilteredBookingDTO filteredBookingDTO) throws Exception;

    BookingDetailDTO bookingDetail(Long id) throws Exception;

    void cancelBooking(Long id, String reason) throws Exception;

    void confirmBookingHost(Long id) throws Exception;

    void rejectBookingHost(Long id, String reason) throws Exception;

    BookingDTO searchFilteredBookingsHost(FilteredBookingsHostDTO getUserBookings) throws Exception;
}
