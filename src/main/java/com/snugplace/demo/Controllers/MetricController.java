package com.snugplace.demo.Controllers;

import com.snugplace.demo.DTO.Metric.MetricHostDTO;
import com.snugplace.demo.DTO.Metric.MetricAccommodationDTO;
import com.snugplace.demo.DTO.Metric.MetricRequestDTO;
import com.snugplace.demo.DTO.ResponseDTO;
import com.snugplace.demo.Service.MetricService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/metrics")
@RequiredArgsConstructor
public class MetricController {

    private final MetricService metricService;

    @PostMapping("/accommodations/{id}")
    public ResponseEntity<ResponseDTO<MetricAccommodationDTO>> getAccommodationMetric(@PathVariable Long id, @RequestBody MetricRequestDTO metricRequestDTO) throws Exception{
        MetricAccommodationDTO metric = metricService.getAccommodationMetric(id, metricRequestDTO.firstDate(), metricRequestDTO.lasDate());
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(false, metric));
    }

    @GetMapping("/metrics/summary")
    public ResponseEntity<ResponseDTO<MetricHostDTO>> getHostMetric(@RequestBody MetricRequestDTO metricRequestDTO) throws Exception {
        MetricHostDTO metric = metricService.getHostMetric(metricRequestDTO.firstDate(), metricRequestDTO.lasDate());
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(false, metric));
    }
}
