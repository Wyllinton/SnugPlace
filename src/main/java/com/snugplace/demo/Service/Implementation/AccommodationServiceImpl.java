package com.snugplace.demo.Service.Implementation;

import com.snugplace.demo.DTO.Accommodation.AccommodationDTO;
import com.snugplace.demo.DTO.Accommodation.CreateAccommodationDTO;
import com.snugplace.demo.DTO.Accommodation.FilterAccommodationDTO;
import com.snugplace.demo.DTO.Comment.CommentDTO;
import com.snugplace.demo.Mappers.AccommodationMapper;
import com.snugplace.demo.Model.Accommodation;
import com.snugplace.demo.Repository.AccommodationRepository;
import com.snugplace.demo.Service.AccommodationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;
import java.util.Optional;


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
    public AccommodationDTO accommodationsDetails(Long id) throws Exception {
        Optional<Accommodation> accommodation = accommodationRepository.findById(id);
        if (accommodation.isPresent()) {
            return accommodationMapper.toAccommodationDTO(accommodation.get());
        }else{
            throw new Exception("No se encontro el alojamiento");
        }
    }

    @Override
    public void updateAccommodation(Long id) throws Exception {

    }

    @Override
    public void deleteAccommodation(Long id) throws Exception {
        Optional<Accommodation> accommodation = accommodationRepository.findById(id);
        if (accommodation.isPresent()) {
            accommodationRepository.deleteById(id);
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
