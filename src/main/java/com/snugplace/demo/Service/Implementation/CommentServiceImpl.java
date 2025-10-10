package com.snugplace.demo.Service.Implementation;

import com.snugplace.demo.DTO.Comment.AnswerCommentDTO;
import com.snugplace.demo.DTO.Comment.CreateCommentDTO;
import com.snugplace.demo.Mappers.CommentMapper;
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
import com.snugplace.demo.Service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

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

    @Override
    public void createComment(CreateCommentDTO createCommentDTO) throws Exception {

        String email = authUtils.getAuthenticatedEmail();
        User user = userRepository.findByEmail(email)
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

        boolean alreadyCommented = commentRepository.existsByUserEmailAndAccommodation_Id(email, accommodation.getId());
        if (alreadyCommented) {
            throw new Exception("Ya has comentado este alojamiento");
        }

        Comment comment = commentMapper.toEntity(createCommentDTO);
        comment.setUser(booking.getUser());
        comment.setAccommodation(accommodation);

        commentRepository.save(comment);
    }

    @Override
    public AnswerCommentDTO answerCommentHost(Long id) throws Exception {
        return null;
    }
}
