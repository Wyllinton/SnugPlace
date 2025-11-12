package com.snugplace.demo.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.snugplace.demo.Controllers.AccommodationController;
import com.snugplace.demo.DTO.Accommodation.*;
import com.snugplace.demo.DTO.Comment.CommentDTO;
import com.snugplace.demo.DTO.ImageDTO;
import com.snugplace.demo.DTO.User.HostDTO;
import com.snugplace.demo.Model.Enums.AccommodationStatus;
import com.snugplace.demo.Model.Enums.Service;
import com.snugplace.demo.Service.AccommodationService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AccommodationController.class)
class AccommodationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccommodationService accommodationService;

    @Autowired
    private ObjectMapper objectMapper;

    private CreateAccommodationDTO createDTO;
    private UpdateAccommodationDTO updateDTO;
    private AccommodationDTO accommodationDTO;
    private FilterAccommodationDTO filterDTO;

    @BeforeEach
    void setUp() {
        HostDTO host = new HostDTO(1L, "Pablo", "pablo@gmail.com");

        ImageDTO imageDTO = new ImageDTO("https://img.com/foto.jpg", "foto.jpg", true);
        Set<ImageDTO> images = Set.of(imageDTO);

        createDTO = new CreateAccommodationDTO(
                host,
                "Apartamento moderno",
                "Hermoso apartamento con vista al mar",
                "Cartagena",
                "Calle 123",
                10.0,
                20.0,
                250000.0,
                4,
                4.5,
                AccommodationStatus.ACTIVE,
                List.of(Service.WIFI, Service.BACKYARD),
                images,
                Set.of()
        );

        updateDTO = new UpdateAccommodationDTO(
                "Apartamento remodelado",
                "Más amplio y cómodo",
                270000.0,
                4,
                Set.of(Service.WIFI, Service.PARKING),
                images
        );

        accommodationDTO = new AccommodationDTO(
                "Apartamento moderno",
                "Hermoso apartamento con vista al mar",
                "Cartagena",
                "Calle 123",
                250000.0,
                4,
                4.5,
                LocalDate.now(),
                Set.of(Service.WIFI, Service.POOL),
                AccommodationStatus.ACTIVE,
                null,
                host,
                List.of()
        );

        filterDTO = new FilterAccommodationDTO(
                "Cartagena",
                LocalDate.now(),
                LocalDate.now().plusDays(3),
                100000.0,
                500000.0,
                2,
                Set.of(Service.WIFI),
                0
        );
    }

    @Test
    void createAccommodation_ReturnsCreated() throws Exception {
        doNothing().when(accommodationService).createAccommodation(any(CreateAccommodationDTO.class));

        mockMvc.perform(post("/accommodations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.error").value(false))
                .andExpect(jsonPath("$.message").value("Alojamiento creado exitosamente"));
    }

    @Test
    void searchFilteredAccommodation_ReturnsList() throws Exception {
        when(accommodationService.searchFilteredAccommodation(any(FilterAccommodationDTO.class)))
                .thenReturn(List.of(accommodationDTO));

        mockMvc.perform(get("/accommodations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(filterDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.error").value(false))
                .andExpect(jsonPath("$.message").value("Consulta exitosa de lista de alojamientos"));
    }

    @Test
    void accommodationsDetails_ReturnsDetails() throws Exception {
        when(accommodationService.accommodationsDetails(1L)).thenReturn(accommodationDTO);

        mockMvc.perform(get("/accommodations/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.error").value(false))
                .andExpect(jsonPath("$.data.title").value("Apartamento moderno"));
    }

    @Test
    void updateAccommodation_ReturnsOk() throws Exception {
        doNothing().when(accommodationService).updateAccommodation(eq(1L), any(UpdateAccommodationDTO.class));

        mockMvc.perform(patch("/accommodations/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Alojamiento actualizado exitosamente"));
    }

    @Test
    void deleteAccommodation_ReturnsOk() throws Exception {
        doNothing().when(accommodationService).deleteAccommodation(1L);

        mockMvc.perform(delete("/accommodations/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Alojamiento eliminado exitosamente"));
    }

    @Test
    void verifyAvailabilityAccommodation_ReturnsAvailability() throws Exception {
        when(accommodationService.verifyAvailabilityAccommodation(anyLong(), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(true);

        mockMvc.perform(get("/accommodations/{id}/availability", 1L)
                        .param("checkIn", "2025-10-10")
                        .param("checkOut", "2025-10-12"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.available").value(true))
                .andExpect(jsonPath("$.message").value("Alojamiento disponible"));
    }

    @Test
    @WithMockUser(authorities = "HOST")
    void myAccommodations_ReturnsList() throws Exception {
        when(accommodationService.myAccommodations(0)).thenReturn(List.of(accommodationDTO));

        mockMvc.perform(get("/accommodations/my-accomodations")
                        .param("page", "0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Consulta exitosa de lista de alojamientos"));
    }

    @Test
    void getAccommodationsComments_ReturnsList() throws Exception {
        when(accommodationService.getAccommodationsComments(1L)).thenReturn(List.of(new CommentDTO()));

        mockMvc.perform(get("/accommodations/{id}/comments", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.error").value(false))
                .andExpect(jsonPath("$.message").value("Consulta exitosa de lista de comentarios"));
    }
}