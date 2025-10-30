package com.snugplace.demo.unit.ServiceImpl;


import com.snugplace.demo.DTO.Accommodation.AccommodationDTO;
import com.snugplace.demo.DTO.Comment.CommentDTO;
import com.snugplace.demo.DTO.Accommodation.CreateAccommodationDTO;
import com.snugplace.demo.DTO.Accommodation.FilterAccommodationDTO;
import com.snugplace.demo.DTO.Accommodation.UpdateAccommodationDTO;
import com.snugplace.demo.Model.Accommodation;
import com.snugplace.demo.Model.Comment;
import com.snugplace.demo.Model.Enums.AccommodationStatus;
import com.snugplace.demo.Mappers.AccommodationMapper;
import com.snugplace.demo.Mappers.CommentMapper;
import com.snugplace.demo.Model.Enums.Service;
import com.snugplace.demo.Model.User;
import com.snugplace.demo.Repository.*;
import com.snugplace.demo.Service.Implementation.AccommodationServiceImpl;
import com.snugplace.demo.Security.AuthUtils;
import jakarta.persistence.*;
import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.data.domain.*;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AccommodationServiceImplTest {

    @Mock private AccommodationMapper accommodationMapper;
    @Mock private CommentMapper commentMapper;
    @Mock private AccommodationRepository accommodationRepository;
    @Mock private UserRepository userRepository;
    @Mock private CommentRepository commentRepository;
    @Mock private EntityManager entityManager;
    @Mock private EntityManagerFactory entityManagerFactory;
    @Mock private BookingRepository bookingRepository;
    @Mock private AuthUtils authUtils;

    @InjectMocks
    private AccommodationServiceImpl accommodationService;

    private Accommodation accommodation;
    private AccommodationDTO accommodationDTO;
    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        accommodation = new Accommodation();
        accommodation.setId(1L);
        accommodation.setTitle("Casa Bonita");
        accommodation.setCity("Cali");
        accommodation.setPriceDay(200.0);
        accommodation.setGuestsCount(4);
        accommodation.setStatus(AccommodationStatus.ACTIVE);

        user = new User();
        user.setId(1L);
        user.setEmail("host@example.com");

        accommodationDTO = new AccommodationDTO("Casa Bonita", "Casa al aire libre", "Bogota", "Carrera 10",200.0, 3, 3.0, null, null, AccommodationStatus.ACTIVE, null, null, null);
    }

    // ✅ 1. Crear alojamiento
    @Test
    @DisplayName("Debe crear un alojamiento correctamente")
    void testCreateAccommodationSuccess() throws Exception {
        CreateAccommodationDTO dto = mock(CreateAccommodationDTO.class);
        when(accommodationMapper.toEntity(dto)).thenReturn(accommodation);

        accommodationService.createAccommodation(dto);

        verify(accommodationRepository).save(accommodation);
    }

    // ✅ 2. Detalles de alojamiento
    @Test
    @DisplayName("Debe devolver los detalles de un alojamiento existente")
    void testAccommodationDetailsSuccess() throws Exception {
        when(accommodationRepository.findWithDetailsById(1L)).thenReturn(Optional.of(accommodation));
        when(accommodationMapper.toAccommodationDTO(accommodation)).thenReturn(accommodationDTO);

        AccommodationDTO result = accommodationService.accommodationsDetails(1L);

        assertNotNull(result);
        assertEquals("Casa Bonita", result.title());
    }

    @Test
    @DisplayName("Debe lanzar excepción si el alojamiento no existe")
    void testAccommodationDetailsNotFound() {
        when(accommodationRepository.findWithDetailsById(1L)).thenReturn(Optional.empty());
        Exception ex = assertThrows(Exception.class, () -> accommodationService.accommodationsDetails(1L));
        assertTrue(ex.getMessage().contains("No se encontro"));
    }

    // ✅ 3. Eliminar alojamiento
    @Test
    @DisplayName("Debe eliminar (desactivar) un alojamiento correctamente")
    void testDeleteAccommodationSuccess() throws Exception {
        when(accommodationRepository.findById(1L)).thenReturn(Optional.of(accommodation));

        accommodationService.deleteAccommodation(1L);

        assertEquals(AccommodationStatus.INACTIVE, accommodation.getStatus());
        verify(accommodationRepository).save(accommodation);
    }

    @Test
    @DisplayName("Debe lanzar excepción si no encuentra alojamiento al eliminar")
    void testDeleteAccommodationNotFound() {
        when(accommodationRepository.findById(1L)).thenReturn(Optional.empty());
        Exception ex = assertThrows(Exception.class, () -> accommodationService.deleteAccommodation(1L));
        assertTrue(ex.getMessage().contains("No se encontro"));
    }

    // ✅ 4. Verificar disponibilidad
    @Test
    @DisplayName("Debe verificar disponibilidad y retornar true si no hay reservas")
    void testVerifyAvailabilityAccommodationTrue() throws Exception {
        when(accommodationRepository.findById(1L)).thenReturn(Optional.of(accommodation));
        when(bookingRepository.findOverlappingBookings(anyLong(), any(), any())).thenReturn(Collections.emptyList());

        boolean result = accommodationService.verifyAvailabilityAccommodation(1L, LocalDate.now(), LocalDate.now().plusDays(3));

        assertTrue(result);
    }

    @Test
    @DisplayName("Debe lanzar excepción si el alojamiento no existe al verificar disponibilidad")
    void testVerifyAvailabilityAccommodationNotFound() {
        when(accommodationRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(Exception.class, () ->
                accommodationService.verifyAvailabilityAccommodation(1L, LocalDate.now(), LocalDate.now().plusDays(3))
        );
    }

    // ✅ 5. Mis alojamientos (MyAccommodations)
    @Test
    @DisplayName("Debe retornar lista de alojamientos del usuario autenticado")
    void testMyAccommodationsSuccess() throws Exception {
        when(authUtils.getAuthenticatedEmail()).thenReturn("host@example.com");
        when(userRepository.findByEmail("host@example.com")).thenReturn(Optional.of(user));

        Page<Accommodation> page = new PageImpl<>(List.of(accommodation));
        when(accommodationRepository.findByUserAndStatus(eq(user), eq(AccommodationStatus.ACTIVE), any(Pageable.class)))
                .thenReturn(page);
        when(accommodationMapper.toAccommodationDTO(accommodation)).thenReturn(accommodationDTO);

        List<AccommodationDTO> result = accommodationService.myAccommodations(0);

        assertEquals(1, result.size());
        assertEquals("Casa Bonita", result.get(0).title());
    }

    // ✅ 6. Comentarios
    @Test
    @DisplayName("Debe retornar los comentarios de un alojamiento existente")
    void testGetAccommodationCommentsSuccess() throws Exception {
        when(accommodationRepository.findById(1L)).thenReturn(Optional.of(accommodation));

        Comment comment = new Comment();
        comment.setId(1L);
        comment.setComment("Excelente lugar");
        List<Comment> comments = List.of(comment);

        when(commentRepository.findByAccommodationId(1L)).thenReturn(comments);

        CommentDTO commentDTO = new CommentDTO(1L,1L,3.0, null, "Excelente lugar", null);
        when(commentMapper.toDTOList(comments)).thenReturn(List.of(commentDTO));

        List<CommentDTO> result = accommodationService.getAccommodationsComments(1L);

        assertEquals(1, result.size());
        assertEquals("Excelente lugar", result.get(0).comment());
    }

    @Test
    @DisplayName("Debe lanzar excepción si no encuentra alojamiento al obtener comentarios")
    void testGetAccommodationCommentsNotFound() {
        when(accommodationRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> accommodationService.getAccommodationsComments(1L));
    }
}

