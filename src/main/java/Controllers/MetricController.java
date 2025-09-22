package Controllers;

import DTO.MetricAccommodationDTO;
import DTO.MetricHostDTO;
import DTO.ResponseDTO;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.Date;

@RestController
@RequestMapping("/metrics")
public class MetricController {

    @GetMapping("/accommodations/{id}")
    public ResponseEntity<ResponseDTO<MetricAccommodationDTO>> getAccomodationMetric(@PathVariable Long id, @RequestParam @NotNull @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date firstDate,  @RequestParam @NotNull @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date lastDate) throws Exception{
        //DEv the logic to get the metric for one accommodation by id
        MetricAccommodationDTO metric = new MetricAccommodationDTO(id,
                "Apartment in Bogotá",
                firstDate,
                lastDate,
                20,
                15,
                3,
                2,
                4.5,
                3200.75);

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(false, metric));
    }

    @GetMapping("/metrics/summary")
    public ResponseEntity<ResponseDTO<MetricHostDTO>> getHostMetric(@NotNull Date firstDate, @NotNull Date lastDate) throws Exception {

        MetricHostDTO metric = new MetricHostDTO();

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(false, metric));
    }
}
