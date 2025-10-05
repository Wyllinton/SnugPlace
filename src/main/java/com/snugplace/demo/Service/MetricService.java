package com.snugplace.demo.Service;

import com.snugplace.demo.DTO.Metric.MetricAccommodationDTO;
import com.snugplace.demo.DTO.Metric.MetricHostDTO;


import java.util.Date;

public interface MetricService {

    MetricAccommodationDTO getAccommodationMetric(Long id, Date firstDate, Date lastDate) throws Exception;

    MetricHostDTO getHostMetric(Long id, Date firstDate, Date lastDate) throws Exception;
}
