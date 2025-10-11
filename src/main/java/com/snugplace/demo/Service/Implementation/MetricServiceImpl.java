package com.snugplace.demo.Service.Implementation;

import com.snugplace.demo.DTO.Metric.MetricHostDTO;
import com.snugplace.demo.DTO.Metric.MetricAccommodationDTO;
import com.snugplace.demo.Model.Accommodation;
import com.snugplace.demo.Model.Booking;
import com.snugplace.demo.Model.Comment;
import com.snugplace.demo.Model.Enums.BookingStatus;
import com.snugplace.demo.Model.User;
import com.snugplace.demo.Repository.AccommodationRepository;
import com.snugplace.demo.Repository.BookingRepository;
import com.snugplace.demo.Repository.CommentRepository;
import com.snugplace.demo.Repository.UserRepository;
import com.snugplace.demo.Security.AuthUtils;
import com.snugplace.demo.Service.MetricService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MetricServiceImpl implements MetricService {

    private final AccommodationRepository accommodationRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    @Autowired
    private AuthUtils authUtils;

    @Override
    public MetricAccommodationDTO getAccommodationMetric(Long id, LocalDate firstDate, LocalDate lastDate) throws Exception {

        // 1️⃣ Buscar el alojamiento
        Accommodation accommodation = accommodationRepository.findById(id)
                .orElseThrow(() -> new Exception("Alojamiento no encontrado"));

        // 2️⃣ Buscar reservas dentro del rango de fechas
        List<Booking> bookings = bookingRepository.findByAccommodationIdAndDateCheckInBetween(id, firstDate, lastDate);

        // 3️⃣ Contadores por estado
        long confirmed = bookings.stream().filter(b -> b.getStatus() == BookingStatus.CONFIRMED).count();
        long cancelled = bookings.stream().filter(b -> b.getStatus() == BookingStatus.CANCELED).count();
        long completed = bookings.stream().filter(b -> b.getStatus() == BookingStatus.COMPLETED).count();

        // 4️⃣ Calcular total de ingresos (solo las confirmadas y completadas)
        double totalIncomes = bookings.stream()
                .filter(b -> b.getStatus() == BookingStatus.CONFIRMED || b.getStatus() == BookingStatus.COMPLETED)
                .mapToDouble(Booking::getPrice)
                .sum();

        // 5️⃣ Calcular promedio de calificaciones
        List<Comment> comments = commentRepository.findByAccommodationId(id);
        double averageRating = comments.isEmpty()
                ? 0.0
                : comments.stream().mapToDouble(Comment::getRating).average().orElse(0.0);

        // 6️⃣ Construir y devolver el DTO
        return new MetricAccommodationDTO(
                accommodation.getId(),
                accommodation.getTitle(),
                firstDate,
                lastDate,
                bookings.size(),
                (int) confirmed,
                (int) cancelled,
                (int) completed,
                averageRating,
                totalIncomes
        );
    }

    @Override
    public MetricHostDTO getHostMetric(LocalDate firstDate, LocalDate lastDate) throws Exception {

        String email = authUtils.getAuthenticatedEmail();
        User host = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // 1️⃣ Obtener todos los alojamientos del host
        List<Accommodation> accommodations = accommodationRepository.findByUser_Id(host.getId());

        if (accommodations.isEmpty()) {
            throw new Exception("El host no tiene alojamientos registrados");
        }

        // 2️⃣ Variables acumulativas globales
        int totalBookings = 0;
        int confirmed = 0;
        int canceled = 0;
        int completed = 0;
        int totalComments = 0;
        double totalRatingSum = 0.0;
        int totalRatingsCount = 0;
        double totalIncomes = 0.0;

        // 3️⃣ Recorrer todos los alojamientos del host
        for (Accommodation acc : accommodations) {

            // 🔹 Obtener reservas por fechas
            List<Booking> bookings = bookingRepository.findByAccommodationIdAndDateCheckInBetween(
                    acc.getId(),
                    firstDate,
                    lastDate
            );

            totalBookings += bookings.size();
            confirmed += bookings.stream().filter(b -> b.getStatus() == BookingStatus.CONFIRMED).count();
            canceled += bookings.stream().filter(b -> b.getStatus() == BookingStatus.CANCELED).count();
            completed += bookings.stream().filter(b -> b.getStatus() == BookingStatus.COMPLETED).count();

            // 🔹 Acumular ingresos (ejemplo: solo las completadas generan ingresos)
            totalIncomes += bookings.stream()
                    .filter(b -> b.getStatus() == BookingStatus.COMPLETED)
                    .mapToDouble(Booking::getPrice) // ajusta si tu campo es diferente
                    .sum();

            // 🔹 Acumular comentarios y calificaciones
            List<Comment> comments = commentRepository.findByAccommodationId(acc.getId());
            totalComments += comments.size();

            for (Comment c : comments) {
                totalRatingSum += c.getRating();
                totalRatingsCount++;
            }
        }

        // 4️⃣ Calcular promedio global de calificación
        double averageRating = totalRatingsCount == 0 ? 0.0 : totalRatingSum / totalRatingsCount;

        // 5️⃣ Construir DTO final
        return new MetricHostDTO(
                accommodations.size(),  // número total de alojamientos
                confirmed,
                canceled,
                completed,
                totalComments,
                totalBookings,
                averageRating,
                (int) totalIncomes // puedes dejarlo como double si prefieres precisión
        );
    }
}
