package com.snugplace.demo.Service.Implementation;

import com.snugplace.demo.DTO.Booking.*;
import com.snugplace.demo.Mappers.BookingMapper;
import com.snugplace.demo.Model.Booking;
import com.snugplace.demo.Model.Enums.BookingStatus;
import com.snugplace.demo.Repository.BookingRepository;
import com.snugplace.demo.Service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;

    @Override
    public void createBooking(CreateBookingDTO createBookingDTO) throws Exception {
        Booking booking = bookingMapper.toEntity(createBookingDTO);
        bookingRepository.save(booking);
    }

    @Override
    public List<BookingDTO> searchFilteredBooking(FilteredBookingDTO filteredBookingDTO) throws Exception {
        /*List<Booking> allBookings = bookingRepository.findAll();

        return allBookings.stream()
                .filter(b -> filteredBookingDTO.titleAccommodation() == null ||
                        b.getAccommodation().getTitle().toLowerCase().contains(filteredBookingDTO.titleAccommodation().toLowerCase()))
                .filter(b -> filteredBookingDTO.description() == null ||
                        b.getAccommodation().getDescription().toLowerCase().contains(filteredBookingDTO.description().toLowerCase()))
                .filter(b -> filteredBookingDTO.id() == null ||
                        b.getUser().getId().equals(filteredBookingDTO.id()))
                .filter(b -> filteredBookingDTO.idAccommodation() == null ||
                        b.getAccommodation().getId().equals(filteredBookingDTO.idAccommodation()))
                .filter(b -> filteredBookingDTO.dateCheckIn() == null ||
                        (b.getDateCheckIn() != null && !b.getDateCheckIn().isBefore(filteredBookingDTO.dateCheckIn())))
                .filter(b -> filteredBookingDTO.dateCheckOut() == null ||
                        (b.getDateCheckOut() != null && !b.getDateCheckOut().isAfter(filteredBookingDTO.dateCheckOut())))
                .filter(b -> filteredBookingDTO.guestsCount() <= 0 ||
                        b.getGuestsCount() == filteredBookingDTO.guestsCount())
                .filter(b -> filteredBookingDTO.price() <= 0 ||
                        b.getPrice() <= filteredBookingDTO.price())
                .map(bookingMapper::toBookingDTO)
                .toList();*/
        return null;
    }

    @Override
    public BookingDetailDTO bookingDetail(Long id) throws Exception {
        return null;
    }

    @Override
    public void cancelBooking(Long id, String reason) throws Exception {
        Optional<Booking> bookingOpt = bookingRepository.findById(id);
        if (bookingOpt.isEmpty()) {
            throw new Exception("Reserva no encontrada");
        }
        Booking booking = bookingOpt.get();
        booking.setStatus(BookingStatus.CANCELED);
        bookingRepository.save(booking);
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
