package Service.Implementation;

import DTO.Booking.*;
import Service.BookingService;

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
    public BookingDetailDTO bookingDetail(String id) throws Exception {
        return null;
    }

    @Override
    public void cancelBooking(String id, String reason) throws Exception {

    }

    @Override
    public void confirmBookingHost(String id) throws Exception {

    }

    @Override
    public void rejectBookingHost(String id, String reason) throws Exception {

    }

    @Override
    public BookingDTO searchFilteredBookingsHost(FilteredBookingsHostDTO getUserBookings) throws Exception {
        return null;
    }
}
