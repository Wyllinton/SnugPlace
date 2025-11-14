package com.snugplace.demo.Service.Implementation;

import com.snugplace.demo.DTO.Booking.*;
import com.snugplace.demo.DTO.Comment.CommentDTO;
import com.snugplace.demo.DTO.User.UserResponseDTO;
import com.snugplace.demo.Mappers.BookingMapper;
import com.snugplace.demo.Mappers.CommentMapper;
import com.snugplace.demo.Mappers.UserMapper;
import com.snugplace.demo.Model.Accommodation;
import com.snugplace.demo.Model.Booking;
import com.snugplace.demo.Model.Comment;
import com.snugplace.demo.Model.Enums.BookingStatus;
import com.snugplace.demo.Model.Enums.Role;
import com.snugplace.demo.Model.User;
import com.snugplace.demo.Repository.AccommodationRepository;
import com.snugplace.demo.Repository.BookingRepository;
import com.snugplace.demo.Repository.CommentRepository;
import com.snugplace.demo.Repository.UserRepository;
import com.snugplace.demo.Security.AuthUtils;
import com.snugplace.demo.Service.AccommodationService;
import com.snugplace.demo.Service.BookingService;
import com.snugplace.demo.Service.MailService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;
    private final UserRepository userRepository;
    private final UserMapper  userMapper;
    private final AccommodationRepository accommodationRepository;
    private final AccommodationService accommodationService;
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final MailService mailService;
    @Autowired
    private AuthUtils authUtils;

    @Override
    public void createBooking(CreateBookingDTO createBookingDTO) throws Exception {

        Long id = authUtils.getAuthenticatedId();
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Accommodation accommodation = accommodationRepository
                .findById(createBookingDTO.idAccommodation())
                .orElseThrow(() -> new Exception("Alojamiento no encontrado"));

        boolean isAvailable = accommodationService.verifyAvailabilityAccommodation(
                createBookingDTO.idAccommodation(),
                Date.valueOf(createBookingDTO.dateCheckIn()).toLocalDate(),
                Date.valueOf(createBookingDTO.dateCheckOut()).toLocalDate()
        );

        if (createBookingDTO.guestsCount() > accommodation.getGuestsCount()) {
            throw new Exception("El número de invitados excede la capacidad del alojamiento");
        }
        if (!createBookingDTO.dateCheckOut().isAfter(createBookingDTO.dateCheckIn())) {
            throw new Exception("La fecha de salida debe ser posterior a la fecha de entrada");
        }
        if(!isAvailable){
            throw new Exception("El alojamiento no está disponible en las fechas seleccionadas");
        }

        long days = java.time.temporal.ChronoUnit.DAYS.between(
                createBookingDTO.dateCheckIn(),
                createBookingDTO.dateCheckOut()
        );

        double calculatedPrice = accommodation.getPriceDay() * days;

        Booking booking = bookingMapper.toEntity(createBookingDTO);

        booking.setStatus(BookingStatus.PENDING);
        booking.setPrice(calculatedPrice);
        booking.setUser(user);

        bookingRepository.save(booking);

        BookingDTO bookingDTO = bookingMapper.toBookingDTO(booking);

        sendBookingConfirmationEmail(bookingDTO, accommodation);
        sendBookingConfirmationEmailHost(bookingDTO, accommodation);
    }

    @Transactional
    @Override
    public List<BookingDTO> searchFilteredBooking(FilteredBookingDTO filteredBookingDTO) throws Exception {

        Long id = authUtils.getAuthenticatedId();
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (user.getRole().equals(Role.HOST)) {
            List<Booking> bookingsHost = bookingRepository.findBookingsByAccommodationUserId(user.getId());
            return bookingsHost.stream()
                    .filter(b -> filteredBookingDTO.titleAccommodation() == null ||
                            b.getAccommodation().getTitle().toLowerCase().contains(filteredBookingDTO.titleAccommodation().toLowerCase()))
                    .filter(b -> filteredBookingDTO.description() == null ||
                            b.getAccommodation().getDescription().toLowerCase().contains(filteredBookingDTO.description().toLowerCase()))
                    .filter(b -> filteredBookingDTO.dateCheckIn() == null ||
                            (b.getDateCheckIn() != null && !b.getDateCheckIn().isBefore(filteredBookingDTO.dateCheckIn())))
                    .filter(b -> filteredBookingDTO.dateCheckOut() == null ||
                            (b.getDateCheckOut() != null && !b.getDateCheckOut().isAfter(filteredBookingDTO.dateCheckOut())))
                    .filter(b -> filteredBookingDTO.guestsCount() == null ||
                            b.getGuestsCount() == filteredBookingDTO.guestsCount())
                    .filter(b -> filteredBookingDTO.price() <= 0 ||
                            b.getPrice() <= filteredBookingDTO.price())
                    .map(bookingMapper::toBookingDTO)
                    .toList();
        }
        if (user.getRole().equals(Role.USER) || user.getRole().equals(Role.GUEST)) {
            List<Booking> bookingsUser = bookingRepository.findBookingsByUserId(user.getId());
            return bookingsUser.stream()
                    .filter(b -> filteredBookingDTO.titleAccommodation() == null ||
                            b.getAccommodation().getTitle().toLowerCase().contains(filteredBookingDTO.titleAccommodation().toLowerCase()))
                    .filter(b -> filteredBookingDTO.description() == null ||
                            b.getAccommodation().getDescription().toLowerCase().contains(filteredBookingDTO.description().toLowerCase()))
                    .filter(b -> filteredBookingDTO.dateCheckIn() == null ||
                            (b.getDateCheckIn() != null && !b.getDateCheckIn().isBefore(filteredBookingDTO.dateCheckIn())))
                    .filter(b -> filteredBookingDTO.dateCheckOut() == null ||
                            (b.getDateCheckOut() != null && !b.getDateCheckOut().isAfter(filteredBookingDTO.dateCheckOut())))
                    .filter(b -> filteredBookingDTO.guestsCount() == null ||
                            b.getGuestsCount() == filteredBookingDTO.guestsCount())
                    .filter(b -> filteredBookingDTO.price() <= 0 ||
                            b.getPrice() <= filteredBookingDTO.price())
                    .map(bookingMapper::toBookingDTO)
                    .toList();
        }
        else{
            List<Booking> allBookings = bookingRepository.findAll();
            return bookingMapper.toBookingDTOList(allBookings);
        }
    }

    @Override
    public BookingDetailDTO bookingDetailHost(Long id) throws Exception {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new Exception("Reserva no encontrada con ID: " + id));

        List<Comment> comments = commentRepository.findByAccommodationId(booking.getAccommodation().getId());

        List<CommentDTO> commentDTOs = comments.stream()
                .map(commentMapper::toCommentDTO)
                .toList();

        UserResponseDTO userDTO = userMapper.toUserResponseDTO(booking.getUser());

        return new BookingDetailDTO(
                userDTO,
                booking.getDateCheckIn(),
                booking.getDateCheckOut(),
                booking.getGuestsCount(),
                booking.getStatus(),
                booking.getPrice(),
                booking.getCreatedAt(),
                commentDTOs
        );
    }

    @Override
    public BookingDetailUserDTO bookingDetail(Long id) throws Exception {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new Exception("Reserva no encontrada con ID: " + id));

        List<Comment> comments = commentRepository.findByAccommodationId(booking.getAccommodation().getId());

        List<CommentDTO> commentDTOs = comments.stream()
                .map(commentMapper::toCommentDTO)
                .toList();

        return new BookingDetailUserDTO(
                booking.getDateCheckIn(),
                booking.getDateCheckOut(),
                booking.getGuestsCount(),
                booking.getStatus(),
                booking.getPrice(),
                booking.getCreatedAt(),
                commentDTOs
        );
    }

    @Override
    public void cancelBooking(Long id, String reason) throws Exception {
        Optional<Booking> bookingOpt = bookingRepository.findById(id);
        if (bookingOpt.isEmpty()) {
            throw new Exception("Reserva no encontrada");
        }
        Booking booking = bookingOpt.get();
        booking.setStatus(BookingStatus.CANCELED);
        bookingRepository.save(booking);
    }

    @Override
    public void confirmBooking(Long id) throws Exception {
        Long idHost = authUtils.getAuthenticatedId();
        User host = userRepository.findById(idHost)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new Exception("Reserva no encontrada"));

        if (!booking.getAccommodation().getUser().getId().equals(host.getId())) {
            throw new Exception("No eres el dueño del alojamiento");
        }

        if (booking.getStatus() != BookingStatus.PENDING) {
            throw new Exception("Solo se pueden confirmar reservas PENDING");
        }

        booking.setStatus(BookingStatus.CONFIRMED);
        bookingRepository.save(booking);
    }

    @Override
    public void cancelBookingByHost(Long id) throws Exception {

        Long idHost = authUtils.getAuthenticatedId();
        User host = userRepository.findById(idHost)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new Exception("Reserva no encontrada"));

        if (!booking.getAccommodation().getUser().getId().equals(host.getId())) {
            throw new Exception("No eres el dueño del alojamiento");
        }

        if (booking.getStatus() != BookingStatus.PENDING) {
            throw new Exception("Solo se pueden cancelar reservas PENDING");
        }

        LocalDate today = LocalDate.now();
        if (!today.isBefore(booking.getDateCheckIn().minusDays(1))) {
            throw new Exception("Solo se pueden cancelar 24 horas antes del check-in");
        }

        booking.setStatus(BookingStatus.CANCELED);
        bookingRepository.save(booking);
    }

    //------------------------------------------------------------------------------------------------------------------

    private void sendBookingConfirmationEmail(BookingDTO bookingDTO, Accommodation accommodation) {
        String subject = "🏡 Confirmación de reserva en SnugPlace";

        String textContent = """
        ¡Hola %s!

        ==================================
        🏠 DETALLES DE TU RESERVA
        ==================================

        Has realizado una nueva reserva en SnugPlace.

        📍 Alojamiento: %s
        🗺️ Ubicación: %s
        👤 Reservado por: %s
        📅 Check-in: %s
        📅 Check-out: %s
        👥 Cantidad de huéspedes: %d
        💵 Precio total: $%.2f

        ✅ Tu reserva ha sido confirmada con éxito.

        ---
        Gracias por confiar en SnugPlace.
        Este es un mensaje automático, por favor no respondas.
        """.formatted(
                bookingDTO.user().name(),
                accommodation.getTitle(),
                accommodation.getCity(),
                bookingDTO.user().name(),
                bookingDTO.dateCheckIn(),
                bookingDTO.dateCheckOut(),
                bookingDTO.guestsCount(),
                bookingDTO.price()
        );

        mailService.sendSimpleEmail(bookingDTO.user().email(), subject, textContent);
    }

    private void sendBookingConfirmationEmailHost(BookingDTO bookingDTO, Accommodation accommodation) {
        String subject = "🏡 Confirmación de reserva en SnugPlace";

        String textContent = """
        ¡Hola %s!

        ==================================
        🏠 DETALLES DE LA RESERVA
        ==================================

        Han realizado una reserva en su alojamiento %s

        📍 Alojamiento: %s
        🗺️ Ubicación: %s
        👤 Reservado por: %s
        📅 Check-in: %s
        📅 Check-out: %s
        👥 Cantidad de huéspedes: %d
        💵 Precio total: $%.2f

        ✅ Se realizó la reserva con éxito en estado PENDIENTE.

        ---
        Gracias por confiar en SnugPlace.
        Este es un mensaje automático, por favor no respondas.
        """.formatted(
                accommodation.getUser().getName(),
                accommodation.getTitle(),
                accommodation.getTitle(),
                accommodation.getCity(),
                bookingDTO.user().name(),
                bookingDTO.dateCheckIn(),
                bookingDTO.dateCheckOut(),
                bookingDTO.guestsCount(),
                bookingDTO.price()
        );

        mailService.sendSimpleEmail(bookingDTO.user().email(), subject, textContent);
    }

    @Override
    public List<BookingDTO> getMyBookings() throws Exception {
        Long id = authUtils.getAuthenticatedId();
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        List<Booking> bookings;

        if (user.getRole().equals(Role.HOST)) {
            // 🔄 COMBINAR: Reservas como HOST + Reservas como HUÉSPED
            List<Booking> bookingsAsHost = bookingRepository.findBookingsByAccommodationUserId(user.getId());
            List<Booking> bookingsAsGuest = bookingRepository.findBookingsByUserId(user.getId());

            System.out.println("🏠 Host " + user.getName() + ":");
            System.out.println("   - Reservas en sus alojamientos: " + bookingsAsHost.size());
            System.out.println("   - Reservas que ha hecho como huésped: " + bookingsAsGuest.size());

            // Combinar ambas listas
            List<Booking> combinedBookings = new ArrayList<>();
            combinedBookings.addAll(bookingsAsHost);
            combinedBookings.addAll(bookingsAsGuest);

            // Ordenar por fecha de creación descendente
            bookings = combinedBookings.stream()
                    .sorted((b1, b2) -> b2.getCreatedAt().compareTo(b1.getCreatedAt()))
                    .toList();

        } else {
            // USER normal: solo sus reservas como huésped, ordenadas
            bookings = bookingRepository.findBookingsByUserId(user.getId()).stream()
                    .sorted((b1, b2) -> b2.getCreatedAt().compareTo(b1.getCreatedAt()))
                    .toList();
        }

        System.out.println("📅 Total de reservas a mostrar: " + bookings.size());

        // Mapear a DTO con el nuevo campo isMyOwnBooking
        return bookings.stream()
                .map(booking -> this.mapToBookingDTOWithContext(booking, user.getId()))
                .toList();
    }

    // Método auxiliar mejorado
    private BookingDTO mapToBookingDTOWithContext(Booking booking, Long currentUserId) {
        System.out.println("🔄 Mapeando booking ID: " + booking.getId());

        // Determinar si es una reserva propia del usuario actual
        boolean isMyOwnBooking = booking.getUser().getId().equals(currentUserId);

        System.out.println("   - Usuario reserva: " + booking.getUser().getId() + " - " + booking.getUser().getName());
        System.out.println("   - Usuario actual: " + currentUserId);
        System.out.println("   - Es mi reserva: " + isMyOwnBooking);

        // Crear UserResponseDTO
        UserResponseDTO userDTO = new UserResponseDTO(
                booking.getUser().getName(),
                booking.getUser().getEmail(),
                booking.getUser().getPhoneNumber()
        );

        return new BookingDTO(
                booking.getId(),
                booking.getAccommodation().getId(),
                userDTO,
                booking.getDateCheckIn(),
                booking.getDateCheckOut(),
                booking.getGuestsCount(),
                booking.getStatus(),
                booking.getPrice(),
                List.of(), // Lista vacía de comments
                isMyOwnBooking // NUEVO: contexto de la reserva
        );
    }
}
