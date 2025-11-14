package com.snugplace.demo.Service;

import com.snugplace.demo.DTO.Booking.*;

import java.util.List;

public interface BookingService {

    void createBooking(CreateBookingDTO createBookingDTO) throws Exception;

    List<BookingDTO> searchFilteredBooking(FilteredBookingDTO filteredBookingDTO) throws Exception;

    BookingDetailDTO bookingDetailHost(Long id) throws Exception;

    BookingDetailUserDTO bookingDetail(Long id) throws Exception;

    void cancelBooking(Long id, String reason) throws Exception;

    void confirmBooking(Long id) throws Exception;

    void cancelBookingByHost(Long id) throws Exception;

    List<BookingDTO> getMyBookings() throws Exception;
}
