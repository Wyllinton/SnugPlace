package com.snugplace.demo.Controllers;

import com.snugplace.demo.DTO.Accommodation.AccommodationDTO;
import com.snugplace.demo.DTO.Accommodation.CreateAccommodationDTO;
import com.snugplace.demo.DTO.Accommodation.FilterAccommodationDTO;
import com.snugplace.demo.DTO.Accommodation.UpdateAccommodationDTO;
import com.snugplace.demo.DTO.Comment.CommentDTO;
import com.snugplace.demo.DTO.ResponseDTO;
import com.snugplace.demo.DTO.ResponseListDTO;
import com.snugplace.demo.Model.Accommodation;
import com.snugplace.demo.Service.AccommodationService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;

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
    public ResponseEntity<ResponseListDTO<List<AccommodationDTO>>> searchFilteredAccommodation(@Valid @RequestBody FilterAccommodationDTO filterAccommodationDTO) throws Exception{
        List<AccommodationDTO> accommodations = accommodationService.searchFilteredAccommodation(filterAccommodationDTO);
        return ResponseEntity.ok(new ResponseListDTO<>(false, "Consulta exitosa de lista de alojamientos", accommodations));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<AccommodationDTO>> accommodationsDetails(@PathVariable Long id) throws Exception{
        AccommodationDTO accommodationDetails = accommodationService.accommodationsDetails(id);
        return ResponseEntity.ok(new ResponseDTO<>(false, accommodationDetails));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ResponseDTO<String>> updateAccommodation(@PathVariable Long id, @Valid @RequestBody UpdateAccommodationDTO updateAccommodationDTO) throws Exception{
        accommodationService.updateAccommodation(id, updateAccommodationDTO);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(false,"Alojamiento actualizado exitosamente"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO<String>> deleteAccommodation(@PathVariable Long id) throws Exception{
        accommodationService.deleteAccommodation(id);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(false,"Alojamiento eliminado exitosamente"));
    }

    @GetMapping("/{id}/availability")
    public ResponseEntity<Map<String,Object>> verifyAvailabilityAccommodation(@PathVariable Long id, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate checkIn,
                                                                               @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate checkOut) throws Exception{
        boolean available = accommodationService.verifyAvailabilityAccommodation(id, checkIn, checkOut);
        Map<String, Object> response = new HashMap<>();
        response.put("error", false);
        response.put("available", available);
        response.put("message", available ? "Alojamiento disponible" : "Alojamiento no disponible");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/my-accomodations")
    @PreAuthorize("hasAuthority('HOST')")
    public ResponseEntity<ResponseListDTO<List<AccommodationDTO>>> myAccommodations(@RequestParam(defaultValue = "0") Integer page) throws Exception{
        List<AccommodationDTO> accommodations = accommodationService.myAccommodations(page);
        return ResponseEntity.ok(new ResponseListDTO<>(false, "Consulta exitosa de lista de alojamientos", accommodations));
    }

    @GetMapping("/{id}/comments")
    public ResponseEntity<ResponseListDTO<List<CommentDTO>>> getAccommodationsComments(@PathVariable Long id) throws Exception{
        List<CommentDTO> comments = accommodationService.getAccommodationsComments(id);
        return ResponseEntity.ok(new ResponseListDTO<>(false, "Consulta exitosa de lista de comentarios", comments));
    }
}
