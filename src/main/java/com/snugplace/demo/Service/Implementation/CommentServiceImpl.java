package com.snugplace.demo.Service.Implementation;

import com.snugplace.demo.DTO.Comment.AnswerCommentDTO;
import com.snugplace.demo.DTO.Comment.CreateCommentDTO;
import com.snugplace.demo.Mappers.CommentMapper;
import com.snugplace.demo.Model.*;
import com.snugplace.demo.Model.Enums.BookingStatus;
import com.snugplace.demo.Repository.*;
import com.snugplace.demo.Security.AuthUtils;
import com.snugplace.demo.Service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final UserRepository userRepository;
    @Autowired
    private AuthUtils authUtils;

    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final BookingRepository bookingRepository;
    private final AccommodationRepository accommodationRepository;
    private final AnswerCommentRepository answerCommentRepository;

    @Override
    public void createComment(CreateCommentDTO createCommentDTO) throws Exception {

        Long id = authUtils.getAuthenticatedId();
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Booking booking = bookingRepository.findById(createCommentDTO.idBooking())
                .orElseThrow(() -> new Exception("Reserva no encontrada"));

        if (!booking.getUser().getEmail().equals(user.getEmail())) {
            throw new Exception("No puedes comentar una reserva que no te pertenece");
        }

        if (booking.getStatus() != BookingStatus.COMPLETED) {
            throw new Exception("Solo puedes comentar alojamientos con reservas completadas");
        }

        Accommodation accommodation = booking.getAccommodation();

        boolean alreadyCommented = commentRepository.existsByUserIdAndAccommodation_Id(id, accommodation.getId());
        if (alreadyCommented) {
            throw new Exception("Ya has comentado este alojamiento");
        }

        Comment comment = commentMapper.toEntity(createCommentDTO);
        comment.setDate(LocalDate.now());
        comment.setUser(booking.getUser());
        comment.setAccommodation(accommodation);

        commentRepository.save(comment);
    }

    @Override
    public void answerCommentHost(AnswerCommentDTO answerCommentDTO) throws Exception {

        // 1️⃣ Obtener id del usuario autenticado (el host)
        Long id = authUtils.getAuthenticatedId();

        User user = userRepository.findById(id)
                .orElseThrow(() -> new Exception("Usuario no encontrado"));

        // 2️⃣ Buscar el comentario al que se quiere responder
        Comment comment = commentRepository.findById(answerCommentDTO.idComment())
                .orElseThrow(() -> new Exception("Comentario no encontrado"));

        // 3️⃣ Verificar que el comentario pertenece a un alojamiento del host autenticado
        Accommodation accommodation = comment.getAccommodation();
        if (!accommodation.getUser().getEmail().equals(user.getEmail())) {
            throw new Exception("No puedes responder un comentario de un alojamiento que no te pertenece");
        }

        // 4️⃣ Verificar que el comentario aún no tenga una respuesta
        boolean alreadyAnswered = answerCommentRepository.existsByCommentId(comment.getId());
        if (alreadyAnswered) {
            throw new Exception("Este comentario ya ha sido respondido");
        }

        // 5️⃣ Validar que la respuesta no esté vacía
        if (answerCommentDTO.answer() == null || answerCommentDTO.answer().isBlank()) {
            throw new Exception("La respuesta no puede estar vacía");
        }

        // 6️⃣ Crear y guardar la respuesta
        AnswerComment answer = new AnswerComment();
        answer.setComment(comment);
        answer.setUser(comment.getUser());
        answer.setAnswer(answerCommentDTO.answer());
        answer.setDate(LocalDate.now());

        answerCommentRepository.save(answer);
    }
}
