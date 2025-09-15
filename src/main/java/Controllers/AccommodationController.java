package Controllers;

import DTO.*;
import Model.Accommodation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;

@RestController()
@RequestMapping("/accommodations")
public class AccommodationController {

    @PostMapping
    public ResponseEntity<ResponseDTO<String>> createAccommodation(@Valid @RequestBody CreateAccommodationDTO createAccommodationDTO) throws Exception{
        //Lógica

        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDTO<>(false, "\t\n" + "Alojamiento creado exitosamente"));
    }

    /* Se debe tener la lógica para que no de error, mientras lo documentaré
    @GetMapping
    public ResponseEntity<ResponseDTO<ArrayList<AccommodationDTO>>> searchFilteredAccommodation(@Valid @RequestBody AccommodationDTO accommodationDTO) throws Exception{
        //Lógica
        ArrayList<Accommodation> accommodations = new ArrayList<>();
        return ResponseEntity.ok(new ResponseDTO<>(false, accommodations ));
    }
    */

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<String>> accommodationsDetails(@PathVariable String id) throws Exception{
        //Lógica

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(false, "\t\n" + "Detalle del alojamiento"));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ResponseDTO<String>> updateAccommodation(@PathVariable String id) throws Exception{
        //Lógica

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(false, "\t\n" + "\t\n" + "Alojamiento actualizado exitosamente"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO<String>> deleteAccommodation(@PathVariable String id) throws Exception{
        //Lógica

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(false, "\t\n" + "\t\n" + "\t\n" + "Alojamiento eliminado exitosamente"));
    }

    /*
    @GetMapping("/{id}/availability")
    public ResponseEntity<ResponseDTO<String>> verifyAvailabilityAccommodation(@PathVariable String id, @NotNull Date checkIn, @NotNull Date checkOut) throws Exception{
        //Lógica

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(false, "\t\n" + "\t\n" + "\t\n" + "\t\n" + "Estado de disponibilidad"));
    }
    */

    /*
    @GetMapping("/my-accomodations")
    public ResponseEntity<ResponseDTO<ArrayList<AccommodationDTO>>> myAccommodations(@Valid @RequestBody AccommodationDTO accommodationDTO) throws Exception{
        //Lógica
        ArrayList<Accommodation> accommodations = new ArrayList<>();
        return ResponseEntity.ok(new ResponseDTO<>(false, accommodations ));
    }
    */

    /*
    @GetMapping("/{id}/comments")
    public ResponseEntity<ResponseDTO<ArrayList<CommentDTO>>> getAccommodationsComments(@PathVariable String id) throws Exception{
        //Lógica
        ArrayList<Accommodation> comments = new ArrayList<>();
        return ResponseEntity.ok(new ResponseDTO<>(false, comments ));
    }
    */
}
