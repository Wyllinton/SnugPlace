package com.snugplace.demo.Service.Implementation;

import com.snugplace.demo.Model.Booking;
import com.snugplace.demo.Model.Enums.BookingStatus;
import com.snugplace.demo.Model.Enums.TypeNotification;
import com.snugplace.demo.Repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor

public class ReminderScheduler {

    private final BookingRepository bookingRepository;
    private final NotificationServiceImpl notificationServiceImpl;

    @Scheduled(cron = "0 0 9 * * *") //Checking at 9 AM everyday
    public void sendCheckInReminders() {
        LocalDate reminderDate = LocalDate.now().plusDays(1);

        List<Booking> upcomingBookings = bookingRepository
                .findByCheckInDateAndStatus(reminderDate, BookingStatus.CONFIRMED);

        for (Booking booking : upcomingBookings) {
            sendGuestReminder(booking);
            sendHostReminder(booking);
        }
    }

    private void sendGuestReminder(Booking booking) {
        String title = "Recordatorio de tu estadía en Snugplace 🏡";
        String message = String.format(
                "Hola %s, tu estadía en '%s' comienza mañana (%s). ¡Prepárate para disfrutar!",
                booking.getUser().getName(),
                booking.getAccommodation().getTitle(),
                booking.getDateCheckIn()
        );

        notificationServiceImpl.sendNotification(
                booking.getUser(),
                title,
                message,
                TypeNotification.REMINDER
        );
    }

    private void sendHostReminder(Booking booking) {
        String title = "Llega un huésped mañana 👋";
        String message = String.format(
                "Hola %s, recuerda que mañana llega %s a tu alojamiento '%s'.",
                booking.getAccommodation().getUser().getName(),
                booking.getUser().getName(),
                booking.getAccommodation().getTitle()
        );

        notificationServiceImpl.sendNotification(
                booking.getAccommodation().getUser(),
                title,
                message,
                TypeNotification.REMINDER
        );
    }
}
