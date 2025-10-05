package com.snugplace.demo.Controllers;

import com.snugplace.demo.DTO.Metric.MetricDTO;
import com.snugplace.demo.DTO.Metric.MetricHostDTO;
import com.snugplace.demo.DTO.ResponseDTO;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/metrics")
public class MetricController {

    @GetMapping("/accommodations/{id}")
    public ResponseEntity<ResponseDTO<MetricDTO>> getAccomodationMetric(@PathVariable Long id, @NotNull Date firstDate, @NotNull Date lastDate) throws Exception{
        //Lógica

    return null;
        //return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(false, metric));
    }

    @GetMapping("/metrics/summary")
    public ResponseEntity<ResponseDTO<MetricHostDTO>> getHostMetric(@NotNull Date firstDate, @NotNull Date lastDate) throws Exception {

    return null;

        //return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(false, metric));
    }
}
