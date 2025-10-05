package com.snugplace.demo.Controllers;

import com.snugplace.demo.DTO.Metric.MetricHostDTO;
import com.snugplace.demo.DTO.Metric.MetricAccommodationDTO;
import com.snugplace.demo.DTO.ResponseDTO;
import com.snugplace.demo.Service.MetricService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/metrics")
@RequiredArgsConstructor
public class MetricController {

    private final MetricService metricService;

    @GetMapping("/accommodations/{id}")
    public ResponseEntity<ResponseDTO<MetricAccommodationDTO>> getAccommodationMetric(@PathVariable Long id, @NotNull Date firstDate, @NotNull Date lastDate) throws Exception{
        MetricAccommodationDTO metric = metricService.getAccommodationMetric(id, firstDate, lastDate);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(false, metric));
    }

    @GetMapping("/metrics/summary")
    public ResponseEntity<ResponseDTO<MetricHostDTO>> getHostMetric(@PathVariable Long id, @NotNull Date firstDate, @NotNull Date lastDate) throws Exception {
        MetricHostDTO metric = metricService.getHostMetric(id, firstDate, lastDate);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(false, metric));
    }
}
