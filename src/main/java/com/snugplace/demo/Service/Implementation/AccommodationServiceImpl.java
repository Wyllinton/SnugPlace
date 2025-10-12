package com.snugplace.demo.Service.Implementation;

import com.snugplace.demo.DTO.Accommodation.AccommodationDTO;
import com.snugplace.demo.DTO.Accommodation.CreateAccommodationDTO;
import com.snugplace.demo.DTO.Accommodation.FilterAccommodationDTO;
import com.snugplace.demo.DTO.Accommodation.UpdateAccommodationDTO;
import com.snugplace.demo.DTO.Comment.CommentDTO;
import com.snugplace.demo.Mappers.AccommodationMapper;
import com.snugplace.demo.Mappers.CommentMapper;
import com.snugplace.demo.Model.*;
import com.snugplace.demo.Model.Enums.AccommodationStatus;
import com.snugplace.demo.Repository.*;
import com.snugplace.demo.Security.AuthUtils;
import com.snugplace.demo.Service.AccommodationService;
import com.snugplace.demo.Service.ImageService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccommodationServiceImpl implements AccommodationService {

    private final AccommodationMapper accommodationMapper;
    private final CommentMapper commentMapper;
    private final AccommodationRepository accommodationRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final EntityManager entityManagerFactory;
    private final BookingRepository bookingRepository;

    @Autowired
    private AuthUtils authUtils;

    @Override
    public void createAccommodation(CreateAccommodationDTO createAccommodationDTO) throws Exception {
        Accommodation accommodation = accommodationMapper.toEntity(createAccommodationDTO);
        accommodationRepository.save(accommodation);
    }

/*
    @Override
    @Transactional
    public void createAccommodation(CreateAccommodationDTO dto) throws Exception {
        Accommodation accommodation = accommodationMapper.toEntity(dto);

        // Mapea las imágenes ya subidas a Cloudinary
        Set<Image> images = dto.images().stream()
                .map(img -> Image.builder()
                        .url(img.url())
                        .cloudinaryId(img.cloudinaryId())
                        .uploadedTime(LocalDate.now())
                        .isMainImage(img.isMainImage())
                        .accommodationId(accommodation)
                        .build()
                ).collect(Collectors.toSet());

        accommodation.setImages(images);
        accommodationRepository.save(accommodation);
    }*/

    @Override
    public List<AccommodationDTO> searchFilteredAccommodation(FilterAccommodationDTO filterAccommodationDTO) throws Exception {
        EntityManager em = entityManagerFactory.getEntityManagerFactory().createEntityManager();

        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Accommodation> cq = cb.createQuery(Accommodation.class);
            Root<Accommodation> root = cq.from(Accommodation.class);

            List<Predicate> predicates = new ArrayList<>();

            if (filterAccommodationDTO.city() != null && !filterAccommodationDTO.city().isBlank()) {
                predicates.add(cb.equal(cb.lower(root.get("city")), filterAccommodationDTO.city().toLowerCase()));
            }

            if (filterAccommodationDTO.minPrice() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("priceDay"), filterAccommodationDTO.minPrice()));
            }
            if (filterAccommodationDTO.maxPrice() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("priceDay"), filterAccommodationDTO.maxPrice()));
            }

            if (filterAccommodationDTO.guestsCount() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("guestsCount"), filterAccommodationDTO.guestsCount()));
            }

            predicates.add(cb.equal(root.get("status"), AccommodationStatus.ACTIVE));

            if (filterAccommodationDTO.services() != null && !filterAccommodationDTO.services().isEmpty()) {
                Join<Object, Object> joinServices = root.join("services");
                predicates.add(joinServices.in(filterAccommodationDTO.services()));
            }

            LocalDate checkIn = filterAccommodationDTO.checkIn();
            LocalDate checkOut = filterAccommodationDTO.checkOut();

            if (checkIn != null && checkOut != null) {

                Subquery<Long> subquery = cq.subquery(Long.class);
                Root<Booking> bookingRoot = subquery.from(Booking.class);
                subquery.select(bookingRoot.get("accommodation").get("id"));

                Predicate overlap = cb.and(
                        cb.lessThan(bookingRoot.get("checkIn"), checkOut),
                        cb.greaterThan(bookingRoot.get("checkOut"), checkIn)
                );

                subquery.where(overlap);

                predicates.add(cb.not(root.get("id").in(subquery)));
            }

            cq.select(root)
                    .where(cb.and(predicates.toArray(new Predicate[0])))
                    .distinct(true);

            TypedQuery<Accommodation> query = em.createQuery(cq);

            int page = filterAccommodationDTO.page() != null ? filterAccommodationDTO.page() : 0;
            int pageSize = 10;
            query.setFirstResult(page * pageSize);
            query.setMaxResults(pageSize);

            List<Accommodation> result = query.getResultList();

            return result.stream()
                    .map(accommodationMapper::toAccommodationDTO)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            throw new Exception("Error al filtrar alojamientos: " + e.getMessage());
        } finally {
            em.close();
        }
    }

    @Override
    @Transactional
    public AccommodationDTO accommodationsDetails(Long id) throws Exception {
        Optional<Accommodation> accommodation = accommodationRepository.findWithDetailsById(id);
        if (accommodation.isPresent()) {
            return accommodationMapper.toAccommodationDTO(accommodation.get());
        } else {
            throw new Exception("No se encontro el alojamiento");
        }
    }

    @Override
    public void updateAccommodation(Long id, UpdateAccommodationDTO updateAccommodationDTO) throws Exception {
        Optional<Accommodation> accommodationOpt = accommodationRepository.findById(id);
        if (accommodationOpt.isEmpty()) {
            throw new Exception("Alojamiento no encontrado.");
        }
        Accommodation accommodation = accommodationOpt.get();
        accommodation.setTitle(updateAccommodationDTO.title());
        accommodation.setDescription(updateAccommodationDTO.description());
        accommodation.setPriceDay(updateAccommodationDTO.priceDay());
        accommodation.setGuestsCount(updateAccommodationDTO.guestsCount());
        accommodation.setServices(updateAccommodationDTO.services());

        if (updateAccommodationDTO.images() != null && !updateAccommodationDTO.images().isEmpty()) {
            AtomicBoolean first = new AtomicBoolean(true);

            Set<Image> images = updateAccommodationDTO.images().stream()
                    .map(url -> {
                        boolean isMain = first.getAndSet(false);
                        return Image.builder()
                                .url(url.url())
                                .uploadedTime(LocalDate.now())
                                .isMainImage(isMain)
                                .accommodationId(accommodation)
                                .build();
                    })
                    .collect(Collectors.toSet());
            accommodation.setImages(images);
        }
        accommodationRepository.save(accommodation);
    }

    @Override
    public void deleteAccommodation(Long id) throws Exception {
        Optional<Accommodation> accommodation = accommodationRepository.findById(id);
        if (accommodation.isPresent()) {
            Accommodation entity = accommodation.get();
            entity.setStatus(AccommodationStatus.INACTIVE);
            accommodationRepository.save(entity);
            //Add the future booking validation
        }else{
            throw new Exception("No se encontro el alojamiento");
        }
    }

    @Override
    public boolean verifyAvailabilityAccommodation(Long id, LocalDate checkIn, LocalDate checkOut) throws Exception {

        Accommodation accommodation = accommodationRepository.findById(id)
                .orElseThrow(() -> new Exception("El alojamiento con id " + id + " no existe"));

        List<Booking> overlapping = bookingRepository.findOverlappingBookings(
                accommodation.getId(),
                checkIn,
                checkOut
        );

        return overlapping.isEmpty();
    }

    @Transactional
    @Override
    public List<AccommodationDTO> myAccommodations(Integer page) throws Exception {
        String email = authUtils.getAuthenticatedEmail();
        User host = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Pageable pageable = (Pageable) PageRequest.of(page, 10, Sort.by("id").descending());

        Page<Accommodation> accommodationsPage = accommodationRepository
                .findByUserAndStatus(host, AccommodationStatus.ACTIVE, pageable);

        return accommodationsPage.getContent()
                .stream()
                .map(accommodationMapper::toAccommodationDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public List<CommentDTO> getAccommodationsComments(Long id) throws Exception {
        Accommodation accommodation = accommodationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Alojamiento no encontrado"));


        List<Comment> comments = commentRepository.findByAccommodationId(accommodation.getId());

        return commentMapper.toDTOList(comments);
    }
}
