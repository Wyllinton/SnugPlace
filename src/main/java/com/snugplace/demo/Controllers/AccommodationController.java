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
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

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
    public ResponseEntity<PaginatedResponseDTO<List<AccommodationDTO>>> searchFilteredAccommodation(
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String checkIn,
            @RequestParam(required = false) String checkOut,
            @RequestParam(required = false) String minPrice,
            @RequestParam(required = false) String maxPrice,
            @RequestParam(required = false) String guestsCount,
            @RequestParam(required = false) List<String> services,
            @RequestParam(defaultValue = "0") String page) throws Exception {

        System.out.println("🎯 FILTROS RECIBIDOS EN BACKEND:");
        System.out.println("📍 Ciudad: " + city);
        System.out.println("📅 Check-in: " + checkIn);
        System.out.println("📅 Check-out: " + checkOut);
        System.out.println("💰 Precio min: " + minPrice);
        System.out.println("💰 Precio max: " + maxPrice);
        System.out.println("👥 Huéspedes: " + guestsCount);
        System.out.println("🔧 Servicios: " + services);
        System.out.println("📄 Página: " + page);

        // ✅ CONVERSIONES (mantén tu código existente)
        LocalDate checkInDate = null;
        LocalDate checkOutDate = null;

        if (checkIn != null && !checkIn.isEmpty()) {
            try {
                checkInDate = LocalDate.parse(checkIn);
                System.out.println("✅ checkIn convertido: " + checkInDate);
            } catch (Exception e) {
                System.out.println("❌ Error parsing checkIn: " + e.getMessage());
            }
        }

        if (checkOut != null && !checkOut.isEmpty()) {
            try {
                checkOutDate = LocalDate.parse(checkOut);
                System.out.println("✅ checkOut convertido: " + checkOutDate);
            } catch (Exception e) {
                System.out.println("❌ Error parsing checkOut: " + e.getMessage());
            }
        }

        Double minPriceValue = 0.0;
        Double maxPriceValue = 1000000.0;

        if (minPrice != null && !minPrice.isEmpty()) {
            try {
                minPriceValue = Double.parseDouble(minPrice);
                System.out.println("✅ minPrice convertido: " + minPriceValue);
            } catch (Exception e) {
                System.out.println("❌ Error parsing minPrice: " + e.getMessage());
            }
        }

        if (maxPrice != null && !maxPrice.isEmpty()) {
            try {
                maxPriceValue = Double.parseDouble(maxPrice);
                System.out.println("✅ maxPrice convertido: " + maxPriceValue);
            } catch (Exception e) {
                System.out.println("❌ Error parsing maxPrice: " + e.getMessage());
            }
        }

        Integer guestsCountValue = 1;
        if (guestsCount != null && !guestsCount.isEmpty()) {
            try {
                guestsCountValue = Integer.parseInt(guestsCount);
                System.out.println("✅ guestsCount convertido: " + guestsCountValue);
            } catch (Exception e) {
                System.out.println("❌ Error parsing guestsCount: " + e.getMessage());
            }
        }

        Set<Service> servicesSet = new HashSet<>();
        if (services != null && !services.isEmpty()) {
            try {
                servicesSet = services.stream()
                        .map(serviceStr -> {
                            try {
                                return Service.valueOf(serviceStr.toUpperCase());
                            } catch (IllegalArgumentException e) {
                                System.out.println("❌ Servicio no válido: " + serviceStr);
                                return null;
                            }
                        })
                        .filter(service -> service != null)
                        .collect(Collectors.toSet());
                System.out.println("✅ services convertidos: " + servicesSet);
            } catch (Exception e) {
                System.out.println("❌ Error parsing services: " + e.getMessage());
            }
        }

        Integer pageValue = 0;
        if (page != null && !page.isEmpty()) {
            try {
                pageValue = Integer.parseInt(page);
                System.out.println("✅ page convertido: " + pageValue);
            } catch (Exception e) {
                System.out.println("❌ Error parsing page: " + e.getMessage());
            }
        }

        try {
            // ✅ CREAR EL DTO DE FILTROS
            FilterAccommodationDTO filterDTO = new FilterAccommodationDTO(
                    city, checkInDate, checkOutDate, minPriceValue,
                    maxPriceValue, guestsCountValue, servicesSet, pageValue
            );

            System.out.println("✅ DTO creado exitosamente: " + filterDTO);

            // ✅ OBTENER RESULTADO PAGINADO
            Page<AccommodationDTO> accommodationsPage = accommodationService.searchFilteredAccommodation(filterDTO);

            System.out.println("✅ Alojamientos filtrados encontrados: " + accommodationsPage.getContent().size());
            System.out.println("📊 Total elementos: " + accommodationsPage.getTotalElements());
            System.out.println("📄 Total páginas: " + accommodationsPage.getTotalPages());

            // ✅ CREAR RESPUESTA CON EL NUEVO DTO PAGINADO
            PaginatedResponseDTO<List<AccommodationDTO>> response =
                    PaginatedResponseDTO.success(
                            "Consulta exitosa de lista de alojamientos",
                            accommodationsPage.getContent(),
                            accommodationsPage.getTotalElements(),
                            accommodationsPage.getTotalPages(),
                            accommodationsPage.getNumber(),
                            accommodationsPage.getSize()
                    );

            return ResponseEntity.ok(response);

        } catch (ConstraintViolationException e) {
            System.out.println("❌ Error de validación en DTO: " + e.getMessage());

            PaginatedResponseDTO<List<AccommodationDTO>> errorResponse =
                    PaginatedResponseDTO.error("Error en los filtros: " + e.getMessage());

            return ResponseEntity.badRequest().body(errorResponse);

        } catch (Exception e) {
            System.out.println("❌ Error general: " + e.getMessage());
            e.printStackTrace();

            PaginatedResponseDTO<List<AccommodationDTO>> errorResponse =
                    PaginatedResponseDTO.error("Error interno del servidor: " + e.getMessage());

            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @PostMapping("/cards")
    public ResponseEntity<?> getAccommodationCards(
            @RequestBody(required = false) Map<String, Object> filters,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size) {

        try {
            System.out.println("🎯 Endpoint /cards llamado con página: " + page + ", tamaño: " + size);
            System.out.println("🔍 Filtros recibidos: " + filters);

            // Obtener resultados paginados
            Page<AccommodationCardDTO> resultPage = accommodationService.getAccommodationCardsPaginated(filters, page, size);

            System.out.println("✅ Alojamientos encontrados: " + resultPage.getContent().size() +
                    " de " + resultPage.getTotalElements());
            System.out.println("📊 Total páginas: " + resultPage.getTotalPages());

            // Crear respuesta estructurada
            Map<String, Object> response = new HashMap<>();
            response.put("error", false);
            response.put("message", "Alojamientos obtenidos exitosamente");
            response.put("data", resultPage.getContent());
            response.put("totalElements", resultPage.getTotalElements());
            response.put("totalPages", resultPage.getTotalPages());
            response.put("currentPage", resultPage.getNumber());
            response.put("size", resultPage.getSize());

            return ResponseEntity.ok().body(response);

        } catch (Exception e) {
            System.out.println("❌ ERROR en controller /cards: " + e.getMessage());
            e.printStackTrace();

            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", true);
            errorResponse.put("message", "Error: " + e.getMessage());
            errorResponse.put("data", List.of());
            errorResponse.put("totalElements", 0);
            errorResponse.put("totalPages", 0);
            errorResponse.put("currentPage", 0);
            errorResponse.put("size", 0);

            return ResponseEntity.badRequest().body(errorResponse);
        }
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
    public ResponseEntity<PaginatedResponseDTO<List<AccommodationDTO>>> myAccommodations(@RequestParam(defaultValue = "0") Integer page) throws Exception {
        System.out.println("🎯 Endpoint /my-accomodations llamado");

        Page<AccommodationDTO> accommodationsPage = accommodationService.myAccommodations(page);

        System.out.println("📦 Número de alojamientos: " + accommodationsPage.getContent().size());

        // ✅ CORREGIR: Enviar el CONTENT directamente en "data", no el Page completo
        PaginatedResponseDTO<List<AccommodationDTO>> response =
                PaginatedResponseDTO.success(
                        "Consulta exitosa de lista de alojamientos",
                        accommodationsPage.getContent(),  // ← Solo el contenido, no el Page
                        accommodationsPage.getTotalElements(),
                        accommodationsPage.getTotalPages(),
                        accommodationsPage.getNumber(),
                        accommodationsPage.getSize()
                );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/comments")
    public ResponseEntity<ResponseListDTO<List<CommentDTO>>> getAccommodationsComments(@PathVariable Long id) throws Exception{
        List<CommentDTO> comments = accommodationService.getAccommodationsComments(id);
        return ResponseEntity.ok(new ResponseListDTO<>(false, "Consulta exitosa de lista de comentarios", comments));
    }
}