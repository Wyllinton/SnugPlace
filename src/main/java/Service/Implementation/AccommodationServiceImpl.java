package Service.Implementation;

import DTO.Accommodation.AccommodationDTO;
import DTO.Accommodation.CreateAccommodationDTO;
import DTO.Accommodation.FilterAccommodationDTO;
import DTO.Comment.CommentDTO;
import Mappers.AccommodationMapper;
import Model.Accommodation;
import Service.AccommodationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor

public class AccommodationServiceImpl implements AccommodationService {

    private final AccommodationMapper accommodationMapper;
    private final Map<Long, Accommodation> accommodationStore = new ConcurrentHashMap<>();

    @Override
    public void createAccommodation(CreateAccommodationDTO createAccommodationDTO) throws Exception {

        Accommodation newAccommodation = accommodationMapper.toEntity(createAccommodationDTO);
        accommodationStore.put(newAccommodation.getId(), newAccommodation);


    }

    @Override
    public List<AccommodationDTO> searchFilteredAccommodation(FilterAccommodationDTO filterAccommodationDTO) throws Exception {
        return List.of();
    }

    @Override
    public AccommodationDTO accommodationsDetails(String id) throws Exception {
        return null;
    }

    @Override
    public void updateAccommodation(String id) throws Exception {

    }

    @Override
    public void deleteAccommodation(String id) throws Exception {

    }

    @Override
    public void verifyAvailabilityAccommodation(String id, Date checkIn, Date checkOut) throws Exception {

    }

    @Override
    public List<AccommodationDTO> myAccommodations(AccommodationDTO accommodationDTO) throws Exception {
        return List.of();
    }

    @Override
    public List<CommentDTO> getAccommodationsComments(String id) throws Exception {
        return List.of();
    }
}
