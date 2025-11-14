package com.snugplace.demo.Service.Implementation;

import com.snugplace.demo.DTO.Accommodation.*;
import com.snugplace.demo.DTO.Comment.CommentDTO;
import com.snugplace.demo.Mappers.AccommodationMapper;
import com.snugplace.demo.Mappers.CommentMapper;
import com.snugplace.demo.Model.*;
import com.snugplace.demo.Model.Image;
import com.snugplace.demo.Model.Enums.AccommodationStatus;
import com.snugplace.demo.Repository.*;
import com.snugplace.demo.Security.AuthUtils;
import com.snugplace.demo.Service.AccommodationService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
        Long id = authUtils.getAuthenticatedId();
        User host = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Pageable pageable = PageRequest.of(page, 10, Sort.by("id").descending());

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

    @Transactional
    public List<AccommodationCardDTO> searchFilteredAccommodationCards(FilterAccommodationDTO filterAccommodationDTO) throws Exception {
        List<Accommodation> accommodations = searchFilteredAccommodationEntities(filterAccommodationDTO);

        return accommodations.stream()
                .map(this::toAccommodationCardDTO)
                .collect(Collectors.toList());
    }

    private List<Accommodation> searchFilteredAccommodationEntities(FilterAccommodationDTO filterAccommodationDTO) throws Exception {
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
            return result;

        } catch (Exception e) {
            throw new Exception("Error al filtrar alojamientos: " + e.getMessage());
        } finally {
            em.close();
        }
    }

    private AccommodationCardDTO toAccommodationCardDTO(Accommodation accommodation) {
        if (accommodation == null) {
            return null;
        }

        return new AccommodationCardDTO(
                accommodation.getId(),
                accommodation.getTitle() != null ? accommodation.getTitle() : "Sin título",
                accommodation.getCity() != null ? accommodation.getCity() : "Ciudad no especificada",
                accommodation.getPriceDay(),
                calculateAverageRating(accommodation),
                getMainImage(accommodation)
        );
    }

    private double calculateAverageRating(Accommodation accommodation) {
        if (accommodation.getComments() == null || accommodation.getComments().isEmpty()) {
            return 0.0;
        }

        return accommodation.getComments().stream()
                .filter(comment -> comment != null)
                .mapToDouble(Comment::getRating)
                .average()
                .orElse(0.0);
    }

    private Image getMainImage(Accommodation accommodation) {
        if (accommodation.getImages() == null || accommodation.getImages().isEmpty()) {
            Image defaultImage = new Image();
            defaultImage.setUrl("https://images.unsplash.com/photo-1566073771259-6a8506099945?w=600&h=400&fit=crop");
            return defaultImage;
        }

        return accommodation.getImages().stream()
                .filter(image -> image != null && image.isMainImage()) // ✅ Solo verificar si es true
                .findFirst()
                .orElse(accommodation.getImages().iterator().next());
    }

    @Override
    @Transactional
    public List<Accommodation> getAccommodationCards(Map<String, Object> filters) {
        try {
            System.out.println("🎯 Filtros recibidos en service: " + filters);

            if (filters != null && filters.containsKey("maxPrice")) {
                Double maxPrice = null;
                Object priceFilter = filters.get("maxPrice");

                if (priceFilter instanceof Number) {
                    maxPrice = ((Number) priceFilter).doubleValue();
                } else if (priceFilter instanceof String) {
                    maxPrice = Double.parseDouble((String) priceFilter);
                }

                if (maxPrice != null && maxPrice > 0) {
                    System.out.println("💰 Filtrando por precio máximo: " + maxPrice);
                    return accommodationRepository.findAllWithImagesAndCommentsByMaxPrice(maxPrice);
                }
            }

            System.out.println("🔍 Buscando todos los alojamientos activos");
            return accommodationRepository.findAllWithImagesAndComments();

        } catch (Exception e) {
            System.out.println("❌ ERROR en AccommodationService.getAccommodationCards: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error al obtener alojamientos: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public Page<AccommodationCardDTO> getAccommodationCardsPaginated(Map<String, Object> filters, int page, int size) {
        EntityManager em = null;
        try {
            em = entityManagerFactory.getEntityManagerFactory().createEntityManager();

            CriteriaBuilder cb = em.getCriteriaBuilder();

            // ✅ DEBUG 1: Contar TODOS los alojamientos ACTIVOS sin filtros
            CriteriaQuery<Long> allActiveCountQuery = cb.createQuery(Long.class);
            Root<Accommodation> allActiveRoot = allActiveCountQuery.from(Accommodation.class);
            allActiveCountQuery.select(cb.count(allActiveRoot))
                    .where(cb.equal(allActiveRoot.get("status"), AccommodationStatus.ACTIVE));
            Long totalActive = em.createQuery(allActiveCountQuery).getSingleResult();
            System.out.println("🔍 TOTAL alojamientos ACTIVOS en BD: " + totalActive);

            // ✅ DEBUG 2: Listar todos los IDs de alojamientos ACTIVOS
            CriteriaQuery<Long> allActiveIdsQuery = cb.createQuery(Long.class);
            Root<Accommodation> allActiveIdsRoot = allActiveIdsQuery.from(Accommodation.class);
            allActiveIdsQuery.select(allActiveIdsRoot.get("id"))
                    .where(cb.equal(allActiveIdsRoot.get("status"), AccommodationStatus.ACTIVE))
                    .orderBy(cb.desc(allActiveIdsRoot.get("id")));
            List<Long> allActiveIds = em.createQuery(allActiveIdsQuery).getResultList();
            System.out.println("🔍 IDs de alojamientos ACTIVOS: " + allActiveIds);

            // CONSULTA PARA CONTAR TOTAL CON FILTROS
            CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
            Root<Accommodation> countRoot = countQuery.from(Accommodation.class);
            List<Predicate> countPredicates = buildPredicates(filters, cb, countRoot);
            countQuery.select(cb.countDistinct(countRoot))
                    .where(cb.and(countPredicates.toArray(new Predicate[0])));

            Long totalCount = em.createQuery(countQuery).getSingleResult();
            System.out.println("📊 Total elementos encontrados CON FILTROS: " + totalCount);

            // CONSULTA PARA DATOS
            CriteriaQuery<Accommodation> dataQuery = cb.createQuery(Accommodation.class);
            Root<Accommodation> dataRoot = dataQuery.from(Accommodation.class);

            dataRoot.fetch("images", JoinType.LEFT);
            dataRoot.fetch("comments", JoinType.LEFT);

            List<Predicate> dataPredicates = buildPredicates(filters, cb, dataRoot);

            dataQuery.select(dataRoot)
                    .where(cb.and(dataPredicates.toArray(new Predicate[0])))
                    .orderBy(cb.desc(dataRoot.get("id")));

            TypedQuery<Accommodation> query = em.createQuery(dataQuery);

            query.setFirstResult(page * size);
            query.setMaxResults(size);

            List<Accommodation> result = query.getResultList();
            System.out.println("✅ Resultados obtenidos para la página " + page + ": " + result.size());

            // ✅ DEBUG 3: Mostrar qué IDs se están devolviendo
            List<Long> resultIds = result.stream().map(Accommodation::getId).collect(Collectors.toList());
            System.out.println("🔍 IDs en página " + page + ": " + resultIds);

            List<AccommodationCardDTO> content = result.stream()
                    .map(this::toAccommodationCardDTO)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

            return new PageImpl<>(content, PageRequest.of(page, size), totalCount);

        } catch (Exception e) {
            System.out.println("❌ ERROR en getAccommodationCardsPaginated: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error al obtener alojamientos paginados: " + e.getMessage());
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    private List<Predicate> buildPredicates(Map<String, Object> filters, CriteriaBuilder cb, Root<Accommodation> root) {
        List<Predicate> predicates = new ArrayList<>();

        // ✅ SOLO filtrar por estado ACTIVO
        predicates.add(cb.equal(root.get("status"), AccommodationStatus.ACTIVE));

        // ✅ TEMPORAL: Comentar todos los otros filtros para debug
    /*
    if (filters != null) {
        if (filters.containsKey("city") && filters.get("city") != null) {
            String city = (String) filters.get("city");
            if (!city.isBlank()) {
                predicates.add(cb.equal(cb.lower(root.get("city")), city.toLowerCase()));
            }
        }

        if (filters.containsKey("minPrice") && filters.get("minPrice") != null) {
            Double minPrice = convertToDouble(filters.get("minPrice"));
            if (minPrice != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("priceDay"), minPrice));
            }
        }

        if (filters.containsKey("maxPrice") && filters.get("maxPrice") != null) {
            Double maxPrice = convertToDouble(filters.get("maxPrice"));
            if (maxPrice != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("priceDay"), maxPrice));
            }
        }

        if (filters.containsKey("guestsCount") && filters.get("guestsCount") != null) {
            Integer guestsCount = convertToInteger(filters.get("guestsCount"));
            if (guestsCount != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("guestsCount"), guestsCount));
            }
        }
    }
    */

        return predicates;
    }

    private Double convertToDouble(Object value) {
        if (value instanceof Number) {
            return ((Number) value).doubleValue();
        } else if (value instanceof String) {
            try {
                return Double.parseDouble((String) value);
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }

    private Integer convertToInteger(Object value) {
        if (value instanceof Number) {
            return ((Number) value).intValue();
        } else if (value instanceof String) {
            try {
                return Integer.parseInt((String) value);
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }
}