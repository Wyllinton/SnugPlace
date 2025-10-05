package com.snugplace.demo.Service.Implementation;

import com.snugplace.demo.DTO.Booking.*;
import com.snugplace.demo.Service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    @Override
    public void createBooking(CreateBookingDTO createBookingDTO) throws Exception {

    }

    @Override
    public List<BookingDTO> searchFilteredBooking(FilteredBookingDTO filteredBookingDTO) throws Exception {
        return null;
    }

    @Override
    public BookingDetailDTO bookingDetail(Long id) throws Exception {
        return null;
    }

    @Override
    public void cancelBooking(Long id, String reason) throws Exception {

    }

    @Override
    public List<BookingDTO> searchFilteredBookingsHost(Long id) throws Exception {
        return null;
    }

    @Override
    public List<BookingDTO> searchFilteredBookingsUser(Long id) throws Exception {
        return null;
    }
}
