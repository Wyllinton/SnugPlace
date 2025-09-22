package Service;

import DTO.*;
import DTO.Accommodation.AccommodationDTO;
import DTO.Accommodation.CreateAccommodationDTO;
import DTO.Comment.CommentDTO;

import java.util.Date;
import java.util.List;

public interface AccommodationService {

    void createAccommodation(CreateAccommodationDTO createAccommodationDTO) throws Exception;

    List<AccommodationDTO> searchFilteredAccommodation(FilterAccommodationDTO filterAccommodationDTO) throws Exception;

    AccommodationDTO accommodationsDetails(String id) throws Exception;

    void updateAccommodation(String id) throws Exception;

    void deleteAccommodation(String id) throws Exception;

    void verifyAvailabilityAccommodation(String id, Date checkIn, Date checkOut) throws Exception;

    List<AccommodationDTO> myAccommodations(AccommodationDTO accommodationDTO) throws Exception;

    List<CommentDTO> getAccommodationsComments(String id) throws Exception;
}
