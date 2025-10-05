package com.snugplace.demo.Service.Implementation;

import com.snugplace.demo.DTO.Metric.MetricHostDTO;
import com.snugplace.demo.DTO.MetricAccommodationDTO;
import com.snugplace.demo.Service.MetricService;

import java.util.Date;

public class MetricServiceImpl implements MetricService {
    @Override
    public MetricAccommodationDTO getAccommodationMetric(Long id, Date firstDate, Date lastDate) throws Exception {
        return null;
    }

    @Override
    public MetricHostDTO getHostMetric(Date firstDate, Date lastDate) throws Exception {
        return null;
    }
}
