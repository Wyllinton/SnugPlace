package com.snugplace.demo.Controllers;

import com.snugplace.demo.DTO.Booking.*;
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

    @GetMapping("/{id}/detail")
    public ResponseEntity<ResponseDTO<BookingDetailDTO>> bookingDetailHost(@PathVariable Long id) throws Exception{
        BookingDetailDTO bookingDetails = bookingService.bookingDetailHost(id);
        return ResponseEntity.ok(new ResponseDTO<>(false, bookingDetails));
    }

    @GetMapping("/{id}/detail-user")
    public ResponseEntity<ResponseDTO<BookingDetailUserDTO>> bookingDetail(@PathVariable Long id) throws Exception{
        BookingDetailUserDTO bookingDetails = bookingService.bookingDetail(id);
        return ResponseEntity.ok(new ResponseDTO<>(false, bookingDetails));
    }

    @PutMapping("/{id}/confirm")
    public ResponseEntity<ResponseDTO<String>> confirmBooking(@PathVariable Long id) throws Exception {
        bookingService.confirmBooking(id);
        return ResponseEntity.ok(new ResponseDTO<>(false, "Reserva confirmada"));
    }

    @PutMapping("/{id}/cancel-by-host")
    public ResponseEntity<ResponseDTO<String>> cancelBookingByHost(@PathVariable Long id) throws Exception {
        bookingService.cancelBookingByHost(id);
        return ResponseEntity.ok(new ResponseDTO<>(false, "Reserva cancelada por el host"));
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<ResponseDTO<String>> cancelBooking(@PathVariable Long id, String reason) throws Exception{
        bookingService.cancelBooking(id, reason);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(false, "Reserva cancelada exitosamente"));
    }
}
