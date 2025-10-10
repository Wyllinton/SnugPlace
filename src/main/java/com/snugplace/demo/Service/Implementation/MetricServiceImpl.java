package com.snugplace.demo.Service.Implementation;

import com.snugplace.demo.DTO.Metric.MetricHostDTO;
import com.snugplace.demo.DTO.Metric.MetricAccommodationDTO;
import com.snugplace.demo.Service.MetricService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class MetricServiceImpl implements MetricService {

    @Override
    public MetricAccommodationDTO getAccommodationMetric(Long id, Date firstDate, Date lastDate) throws Exception {
        //MetricAccommodationDTO metricAccommodation = new MetricAccommodationDTO();
        return null;
    }

    @Override
    public MetricHostDTO getHostMetric(Long id, Date firstDate, Date lastDate) throws Exception {
        return null;
    }
}
