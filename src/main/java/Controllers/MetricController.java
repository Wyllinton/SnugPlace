package Controllers;

import DTO.Metric.MetricDTO;
import DTO.Metric.MetricHostDTO;
import DTO.ResponseDTO;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<ResponseDTO<MetricDTO>> getAccomodationMetric(@PathVariable String id, @NotNull Date firstDate, @NotNull Date lastDate) throws Exception{
        //Lógica
        MetricDTO metric = new MetricDTO();

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(false, metric));
    }

    @GetMapping("/metrics/summary")
    public ResponseEntity<ResponseDTO<MetricHostDTO>> getHostMetric(@NotNull Date firstDate, @NotNull Date lastDate) throws Exception {

        MetricHostDTO metric = new MetricHostDTO();

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(false, metric));
    }
}
