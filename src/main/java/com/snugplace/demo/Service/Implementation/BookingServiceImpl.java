package com.snugplace.demo.Service.Implementation;

import com.snugplace.demo.DTO.Booking.*;
import com.snugplace.demo.Service.BookingService;

import java.util.List;

public class BookingServiceImpl implements BookingService {
    @Override
    public void createBooking(CreateBookingDTO createBookingDTO) throws Exception {

    }

    @Override
    public List<BookingDTO> searchFilteredBooking(FilteredBookingDTO filteredBookingDTO) throws Exception {
        return List.of();
    }

    @Override
    public BookingDetailDTO bookingDetail(Long id) throws Exception {
        return null;
    }

    @Override
    public void cancelBooking(Long id, String reason) throws Exception {

    }

    @Override
    public void confirmBookingHost(Long id) throws Exception {

    }

    @Override
    public void rejectBookingHost(Long id, String reason) throws Exception {

    }

    @Override
    public BookingDTO searchFilteredBookingsHost(FilteredBookingsHostDTO getUserBookings) throws Exception {
        return null;
    }
}
