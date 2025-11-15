package com.snugplace.demo.Service;

import com.snugplace.demo.DTO.Accommodation.*;
import com.snugplace.demo.DTO.Comment.CommentDTO;
import com.snugplace.demo.Model.Accommodation;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface AccommodationService {

    void createAccommodation(CreateAccommodationDTO createAccommodationDTO) throws Exception;

    Page<AccommodationDTO> searchFilteredAccommodation(FilterAccommodationDTO filterAccommodationDTO) throws Exception;

    AccommodationDTO accommodationsDetails(Long id) throws Exception;

    void updateAccommodation(Long id, UpdateAccommodationDTO updateAccommodationDTO) throws Exception;

    void deleteAccommodation(Long id) throws Exception;

    boolean verifyAvailabilityAccommodation(Long id, LocalDate checkIn, LocalDate checkOut) throws Exception;

    Page<AccommodationDTO> myAccommodations(Integer page) throws Exception;

    List<CommentDTO> getAccommodationsComments(Long id) throws Exception;

    List<AccommodationCardDTO> searchFilteredAccommodationCards(FilterAccommodationDTO filterAccommodationDTO) throws Exception;

    List<Accommodation> getAccommodationCards(Map<String, Object> filters);

    Page<AccommodationCardDTO> getAccommodationCardsPaginated(Map<String, Object> filters, int page, int size);
}