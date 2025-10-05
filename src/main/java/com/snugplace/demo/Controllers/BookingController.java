package com.snugplace.demo.Controllers;

import com.snugplace.demo.DTO.Booking.BookingDTO;
import com.snugplace.demo.DTO.Booking.CreateBookingDTO;
import com.snugplace.demo.DTO.Booking.FilteredBookingDTO;
import com.snugplace.demo.DTO.ResponseDTO;
import com.snugplace.demo.Service.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<ResponseDTO<String>> createBooking(@Valid @RequestBody CreateBookingDTO createBookingDTO) throws Exception{
        bookingService.createBooking(createBookingDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDTO<>(false, "Reserva creada exitosamente"));
    }

    @GetMapping
    public ResponseEntity<ResponseDTO<List<BookingDTO>>> searchFilteredBooking(@Valid @RequestBody FilteredBookingDTO filteredBookingDTO) throws Exception{
        List<BookingDTO> bookings = bookingService.searchFilteredBooking(filteredBookingDTO);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(false, bookings));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<String>> bookingDetail(@PathVariable Long id) throws Exception{
        bookingService.bookingDetail(id);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(false,"Detalle de la reserva"));
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<ResponseDTO<String>> cancelBooking(@PathVariable Long id, String reason) throws Exception{
        bookingService.cancelBooking(id, reason);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(false, "Reserva cancelada exitosamente"));
    }

    @GetMapping("/my-bookings-host/{id}")
    public ResponseEntity<ResponseDTO<List<BookingDTO>>> searchFilteredBookingsHost(@Valid @PathVariable Long id) throws Exception{
        List<BookingDTO> bookings = bookingService.searchFilteredBookingsHost(id);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(false, bookings));
    }

    @GetMapping("/my-bookings-user/{id}")
    public ResponseEntity<ResponseDTO<List<BookingDTO>>> searchFilteredBookingsUser(@Valid @PathVariable Long id) throws Exception{
        List<BookingDTO> bookings = bookingService.searchFilteredBookingsUser(id);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(false, bookings));
    }
}
