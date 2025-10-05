package com.snugplace.demo.Service;

import com.snugplace.demo.DTO.Accommodation.AccommodationDTO;
import com.snugplace.demo.DTO.Accommodation.CreateAccommodationDTO;
import com.snugplace.demo.DTO.Accommodation.FilterAccommodationDTO;
import com.snugplace.demo.DTO.Comment.CommentDTO;


import java.util.Date;
import java.util.List;

public interface AccommodationService {

    void createAccommodation(CreateAccommodationDTO createAccommodationDTO) throws Exception;

    List<AccommodationDTO> searchFilteredAccommodation(FilterAccommodationDTO filterAccommodationDTO) throws Exception;

    AccommodationDTO accommodationsDetails(Long id) throws Exception;

    void updateAccommodation(Long id) throws Exception;

    void deleteAccommodation(Long id) throws Exception;

    boolean verifyAvailabilityAccommodation(Long id, Date checkIn, Date checkOut) throws Exception;

    List<AccommodationDTO> myAccommodations(Integer page) throws Exception;

    List<CommentDTO> getAccommodationsComments(Long id) throws Exception;
}
