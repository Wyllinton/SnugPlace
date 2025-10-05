package com.snugplace.demo.Controllers;

import com.snugplace.demo.DTO.Booking.BookingDTO;
import com.snugplace.demo.DTO.Booking.CreateBookingDTO;
import com.snugplace.demo.DTO.Booking.FilteredBookingDTO;
import com.snugplace.demo.DTO.Booking.FilteredBookingsHostDTO;
import com.snugplace.demo.DTO.ResponseDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    @PostMapping
    public ResponseEntity<ResponseDTO<String>> createBooking(@Valid @RequestBody CreateBookingDTO createBookingDTO) throws Exception{
        //Lógica

        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDTO<>(false, "Reserva creada exitosamente"));
    }

    @GetMapping
    public ResponseEntity<ResponseDTO<ArrayList<BookingDTO>>> searchFilteredBooking(@Valid @RequestBody FilteredBookingDTO filteredBookingDTO) throws Exception{
        //Lógica
        ArrayList<BookingDTO> bookings = new ArrayList<BookingDTO>();
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(false, bookings));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<String>> bookingDetail(@PathVariable Long id) throws Exception{
        //Lógica

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(false, "\t\n" + "Detalle de la reserva"));
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<ResponseDTO<String>> cancelBooking(@PathVariable Long id, String reason) throws Exception{
        //Lógica

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(false, "\t\n" + "\t\n" + "Reserva cancelada exitosamente"));
    }

    @PutMapping("/{id}/confirm")
    public ResponseEntity<ResponseDTO<String>> confirmBookingHost(@PathVariable Long id) throws Exception{
        //Lógica

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(false, "\t\n" + "\t\n" + "\t\n" + "Reserva confirmada exitosamente"));
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<ResponseDTO<String>> rejectBookingHost(@PathVariable Long id, String reason) throws Exception{
        //Lógica

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(false, "\t\n" + "\t\n" + "\t\n" + "\t\n" + "Reserva rechazada exitosamente"));
    }

    @GetMapping("/my-bookings-host")
    public ResponseEntity<ResponseDTO<ArrayList<BookingDTO>>> searchFilteredBookingsHost(@Valid @RequestBody FilteredBookingsHostDTO getUserBookings) throws Exception{
        //Logic
        ArrayList<BookingDTO> bookings = new ArrayList<BookingDTO>();
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(false, bookings));
    }
}
