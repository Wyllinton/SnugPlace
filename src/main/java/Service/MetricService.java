package Service;

import DTO.MetricDTO;
import DTO.MetricHostDTO;
import DTO.ResponseDTO;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Date;

public interface MetricService {

    MetricDTO getAccomodationMetric(String id, Date firstDate, Date lastDate) throws Exception;

    MetricHostDTO getHostMetric(Date firstDate, Date lastDate) throws Exception;
}
