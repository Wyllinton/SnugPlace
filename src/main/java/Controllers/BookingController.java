package Controllers;

import DTO.BookingDTO;
import DTO.CreateBookingDTO;
import DTO.GetUserBookings;
import DTO.ResponseDTO;
import Model.Booking;
import jakarta.validation.Valid;
import org.hibernate.annotations.Parameter;
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

    /*

    @GetMapping
    public ResponseEntity<ResponseDTO<ArrayList<BookingDTO>>> searchFilteredBooking(@Valid @RequestBody GetUserBookings getUserBookings) throws Exception{
        //Lógica
        ArrayList<Booking> bookings = new ArrayList<>();
        return ResponseEntity.ok(new ResponseDTO<>(false, bookings ));
    }
    */

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<String>> bookingDetail(@PathVariable String id) throws Exception{
        //Lógica

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(false, "\t\n" + "Detalle de la reserva"));
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<ResponseDTO<String>> cancelBooking(@PathVariable String id, String reason) throws Exception{
        //Lógica

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(false, "\t\n" + "\t\n" + "Reserva cancelada exitosamente"));
    }

    @PutMapping("/{id}/confirm")
    public ResponseEntity<ResponseDTO<String>> confirmBookingHost(@PathVariable String id) throws Exception{
        //Lógica

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(false, "\t\n" + "\t\n" + "\t\n" + "Reserva confirmada exitosamente"));
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<ResponseDTO<String>> rejectBookingHost(@PathVariable String id, String reason) throws Exception{
        //Lógica

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(false, "\t\n" + "\t\n" + "\t\n" + "\t\n" + "Reserva rechazada exitosamente"));
    }

    /*
    @GetMapping("/my-bookings-host")
    public ResponseEntity<ResponseDTO<ArrayList<BookingDTO>>> searchFilteredBookingsHost(@Valid @RequestBody GetUserBookings getUserBookings) throws Exception{
        //Lógica
        ArrayList<Booking> bookings = new ArrayList<>();
        return ResponseEntity.ok(new ResponseDTO<>(false, bookings ));
    }
    */
}
