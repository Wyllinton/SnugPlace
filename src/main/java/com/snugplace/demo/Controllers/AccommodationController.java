package com.snugplace.demo.Controllers;

import com.snugplace.demo.DTO.Accommodation.*;
import com.snugplace.demo.DTO.Comment.CommentDTO;
import com.snugplace.demo.DTO.ResponseDTO;
import com.snugplace.demo.DTO.ResponseListDTO;
import com.snugplace.demo.Model.Accommodation;
import com.snugplace.demo.Model.Enums.Service;
import com.snugplace.demo.Service.AccommodationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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

    @PostMapping("/create")
    public ResponseEntity<ResponseDTO<String>> createAccommodation(@Valid @RequestBody CreateAccommodationDTO createAccommodationDTO) throws Exception{
        accommodationService.createAccommodation(createAccommodationDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDTO<>(false, "\t\n" + "Alojamiento creado exitosamente"));
    }

    @GetMapping
    public ResponseEntity<ResponseListDTO<List<AccommodationDTO>>> searchFilteredAccommodation(@Valid @RequestBody FilterAccommodationDTO filterAccommodationDTO) throws Exception{
        List<AccommodationDTO> accommodations = accommodationService.searchFilteredAccommodation(filterAccommodationDTO);
        return ResponseEntity.ok(new ResponseListDTO<>(false, "Consulta exitosa de lista de alojamientos", accommodations));
    }

    @PostMapping("/cards")
    public ResponseEntity<?> getAccommodationCards(
            @RequestBody(required = false) Map<String, Object> filters,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size) {

        try {
            System.out.println("🎯 Endpoint /cards llamado con página: " + page + ", tamaño: " + size);

            // Convertir Map a FilterAccommodationDTO
            FilterAccommodationDTO filterDTO = convertMapToFilterDTO(filters, page, size);

            // Obtener resultados paginados
            Page<AccommodationCardDTO> resultPage = accommodationService.getAccommodationCardsPaginated(filterDTO);

            System.out.println("✅ Alojamientos encontrados: " + resultPage.getContent().size() +
                    " de " + resultPage.getTotalElements());

            return ResponseEntity.ok().body(Map.of(
                    "error", false,
                    "message", "Alojamientos obtenidos exitosamente",
                    "data", resultPage.getContent(),
                    "totalElements", resultPage.getTotalElements(),
                    "totalPages", resultPage.getTotalPages(),
                    "currentPage", resultPage.getNumber(),
                    "size", resultPage.getSize()
            ));

        } catch (Exception e) {
            System.out.println("❌ ERROR en controller /cards: " + e.getMessage());
            e.printStackTrace();

            return ResponseEntity.badRequest().body(Map.of(
                    "error", true,
                    "message", "Error: " + e.getMessage(),
                    "data", List.of(),
                    "totalElements", 0,
                    "totalPages", 0,
                    "currentPage", 0,
                    "size", 0
            ));
        }
    }

    private FilterAccommodationDTO convertMapToFilterDTO(Map<String, Object> filters, int page, int size) {
        try {
            System.out.println("🔄 Convirtiendo Map a FilterAccommodationDTO: " + filters);

            // Extraer y convertir cada campo del Map
            String city = extractString(filters, "city");
            LocalDate checkIn = extractLocalDate(filters, "checkIn");
            LocalDate checkOut = extractLocalDate(filters, "checkOut");
            Double minPrice = extractDouble(filters, "minPrice");
            Double maxPrice = extractDouble(filters, "maxPrice");
            Integer guestsCount = extractInteger(filters, "guestsCount");
            Set<Service> services = extractServices(filters, "services");

            System.out.println("✅ Campos convertidos:");
            System.out.println("   🔹 City: " + city);
            System.out.println("   🔹 CheckIn: " + checkIn);
            System.out.println("   🔹 CheckOut: " + checkOut);
            System.out.println("   🔹 MinPrice: " + minPrice);
            System.out.println("   🔹 MaxPrice: " + maxPrice);
            System.out.println("   🔹 GuestsCount: " + guestsCount);
            System.out.println("   🔹 Services: " + services);
            System.out.println("   🔹 Page: " + page);

            return new FilterAccommodationDTO(
                    city, checkIn, checkOut, minPrice, maxPrice, guestsCount, services, page
            );

        } catch (Exception e) {
            System.out.println("❌ Error al convertir Map a FilterAccommodationDTO: " + e.getMessage());
            e.printStackTrace();
            // Retornar un DTO con valores por defecto en caso de error
            return new FilterAccommodationDTO(null, null, null, null, null, null, null, page);
        }
    }

    private String extractString(Map<String, Object> filters, String key) {
        if (filters == null || !filters.containsKey(key)) {
            return null;
        }
        Object value = filters.get(key);
        if (value instanceof String && !((String) value).isBlank()) {
            return (String) value;
        }
        return null;
    }

    private LocalDate extractLocalDate(Map<String, Object> filters, String key) {
        if (filters == null || !filters.containsKey(key)) {
            return null;
        }
        Object value = filters.get(key);
        try {
            if (value instanceof String && !((String) value).isBlank()) {
                return LocalDate.parse((String) value);
            }
        } catch (Exception e) {
            System.out.println("⚠️ Error parseando fecha para " + key + ": " + value);
        }
        return null;
    }

    private Double extractDouble(Map<String, Object> filters, String key) {
        if (filters == null || !filters.containsKey(key)) {
            return null;
        }
        Object value = filters.get(key);
        try {
            if (value instanceof Number) {
                return ((Number) value).doubleValue();
            } else if (value instanceof String && !((String) value).isBlank()) {
                return Double.parseDouble((String) value);
            }
        } catch (Exception e) {
            System.out.println("⚠️ Error parseando double para " + key + ": " + value);
        }
        return null;
    }

    private Integer extractInteger(Map<String, Object> filters, String key) {
        if (filters == null || !filters.containsKey(key)) {
            return null;
        }
        Object value = filters.get(key);
        try {
            if (value instanceof Number) {
                return ((Number) value).intValue();
            } else if (value instanceof String && !((String) value).isBlank()) {
                return Integer.parseInt((String) value);
            }
        } catch (Exception e) {
            System.out.println("⚠️ Error parseando integer para " + key + ": " + value);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private Set<Service> extractServices(Map<String, Object> filters, String key) {
        if (filters == null || !filters.containsKey(key)) {
            return null;
        }
        Object value = filters.get(key);
        try {
            if (value instanceof List) {
                List<String> serviceStrings = (List<String>) value;
                Set<Service> services = new HashSet<>();
                for (String serviceStr : serviceStrings) {
                    try {
                        Service service = Service.valueOf(serviceStr.toUpperCase());
                        services.add(service);
                    } catch (IllegalArgumentException e) {
                        System.out.println("⚠️ Servicio no válido: " + serviceStr);
                    }
                }
                return services.isEmpty() ? null : services;
            }
        } catch (Exception e) {
            System.out.println("⚠️ Error parseando servicios: " + e.getMessage());
        }
        return null;
    }


    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<AccommodationDTO>> accommodationsDetails(@PathVariable Long id) throws Exception{
        AccommodationDTO accommodationDetails = accommodationService.accommodationsDetails(id);
        return ResponseEntity.ok(new ResponseDTO<>(false, accommodationDetails));
    }

    @PatchMapping("/edit/{id}")
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
