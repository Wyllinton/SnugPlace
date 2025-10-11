package com.snugplace.demo.Service;

import com.snugplace.demo.DTO.Metric.MetricAccommodationDTO;
import com.snugplace.demo.DTO.Metric.MetricHostDTO;


import java.time.LocalDate;
import java.util.Date;

public interface MetricService {

    MetricAccommodationDTO getAccommodationMetric(Long id, LocalDate firstDate, LocalDate lastDate) throws Exception;

    MetricHostDTO getHostMetric(LocalDate firstDate, LocalDate lastDate) throws Exception;
}
