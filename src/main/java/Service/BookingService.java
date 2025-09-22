package Service;

import DTO.Booking.*;

import java.util.List;

public interface BookingService {

    void createBooking(CreateBookingDTO createBookingDTO) throws Exception;

    List<BookingDTO> searchFilteredBooking(FilteredBookingDTO filteredBookingDTO) throws Exception;

    BookingDetailDTO bookingDetail(String id) throws Exception;

    void cancelBooking(String id, String reason) throws Exception;

    void confirmBookingHost(String id) throws Exception;

    void rejectBookingHost(String id, String reason) throws Exception;

    BookingDTO searchFilteredBookingsHost(FilteredBookingsHostDTO getUserBookings) throws Exception;
}
