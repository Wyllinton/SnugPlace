package com.snugplace.demo.Controllers;

import com.snugplace.demo.DTO.Accommodation.AccommodationDTO;
import com.snugplace.demo.DTO.Accommodation.CreateAccommodationDTO;
import com.snugplace.demo.DTO.Accommodation.FilterAccommodationDTO;
import com.snugplace.demo.DTO.Comment.CommentDTO;
import com.snugplace.demo.DTO.ResponseDTO;
import com.snugplace.demo.Model.Accommodation;
import com.snugplace.demo.Service.AccommodationService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;

@RestController()
@RequestMapping("/accommodations")
@RequiredArgsConstructor
public class AccommodationController {

    private final AccommodationService accommodationService;

    @PostMapping
    public ResponseEntity<ResponseDTO<String>> createAccommodation(@Valid @RequestBody CreateAccommodationDTO createAccommodationDTO) throws Exception{
        accommodationService.createAccommodation(createAccommodationDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDTO<>(false, "\t\n" + "Alojamiento creado exitosamente"));
    }

    @GetMapping
    public ResponseEntity<ResponseDTO<ArrayList<AccommodationDTO>>> searchFilteredAccommodation(@Valid @RequestBody FilterAccommodationDTO filterAccommodationDTO) throws Exception{
        //Lógica
        ArrayList accommodations = new ArrayList<Accommodation>();

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(false, accommodations ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<AccommodationDTO>> accommodationsDetails(@PathVariable Long id) throws Exception{
        //Lógica

        return null;
        //return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(false, "\t\n" + "Detalle del alojamiento"));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ResponseDTO<String>> updateAccommodation(@PathVariable Long id) throws Exception{
        //Lógica

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(false, "\t\n" + "\t\n" + "Alojamiento actualizado exitosamente"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO<String>> deleteAccommodation(@PathVariable Long id) throws Exception{
        accommodationService.deleteAccommodation(id);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(false, "\t\n" + "\t\n" + "\t\n" + "Alojamiento eliminado exitosamente"));
    }

    @GetMapping("/{id}/availability")
    public ResponseEntity<ResponseDTO<String>> verifyAvailabilityAccommodation(@PathVariable Long id, @NotNull Date checkIn, @NotNull Date checkOut) throws Exception{
        //Lógica

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(false, "\t\n" + "\t\n" + "\t\n" + "\t\n" + "Estado de disponibilidad"));
    }

    @GetMapping("/my-accomodations")
    public ResponseEntity<ResponseDTO<ArrayList<AccommodationDTO>>> myAccommodations(@NotNull Integer page) throws Exception{
        //Lógica
        ArrayList<AccommodationDTO> accommodations = new ArrayList<AccommodationDTO>();

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(false, accommodations ));
    }

    @GetMapping("/{id}/comments")
    public ResponseEntity<ResponseDTO<ArrayList<CommentDTO>>> getAccommodationsComments(@PathVariable Long id) throws Exception{
        //Lógica
        ArrayList<CommentDTO> comments = new ArrayList<CommentDTO>();

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(false, comments ));
    }
}
