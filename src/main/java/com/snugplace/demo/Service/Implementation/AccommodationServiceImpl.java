package com.snugplace.demo.Service.Implementation;

import com.snugplace.demo.DTO.Accommodation.AccommodationDTO;
import com.snugplace.demo.DTO.Accommodation.CreateAccommodationDTO;
import com.snugplace.demo.DTO.Accommodation.FilterAccommodationDTO;
import com.snugplace.demo.DTO.Accommodation.UpdateAccommodationDTO;
import com.snugplace.demo.DTO.Comment.CommentDTO;
import com.snugplace.demo.Mappers.AccommodationMapper;
import com.snugplace.demo.Model.Accommodation;
import com.snugplace.demo.Model.Enums.AccommodationStatus;
import com.snugplace.demo.Model.Image;
import com.snugplace.demo.Repository.AccommodationRepository;
import com.snugplace.demo.Service.AccommodationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class AccommodationServiceImpl implements AccommodationService {

    private final AccommodationMapper accommodationMapper;
    private final AccommodationRepository accommodationRepository;

    @Override
    public void createAccommodation(CreateAccommodationDTO createAccommodationDTO) throws Exception {
        Accommodation accommodation = accommodationMapper.toEntity(createAccommodationDTO);
        accommodationRepository.save(accommodation);
    }

    @Override
    public List<AccommodationDTO> searchFilteredAccommodation(FilterAccommodationDTO filterAccommodationDTO) throws Exception {
        return List.of();
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
            AtomicBoolean first = new AtomicBoolean(true); // indicador de la primera imagen

            Set<Image> images = updateAccommodationDTO.images().stream()
                    .map(url -> {
                        boolean isMain = first.getAndSet(false); // la primera será true, luego false
                        return Image.builder()
                                .url(url.url())
                                .uploadedTime(LocalDate.now())
                                .isMainImage(isMain)
                                .accommodationId(accommodation) // la entidad padre
                                .build();
                    })
                    .collect(Collectors.toSet());

            // Guardas o agregas estas imágenes al alojamiento
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
    public boolean verifyAvailabilityAccommodation(Long id, Date checkIn, Date checkOut) throws Exception {
        return false;
    }

    @Override
    public List<AccommodationDTO> myAccommodations(Integer page) throws Exception {
        return List.of();
    }

    @Override
    public List<CommentDTO> getAccommodationsComments(Long id) throws Exception {
        return List.of();
    }
}
