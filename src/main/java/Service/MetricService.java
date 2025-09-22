package Service;

import DTO.MetricAccommodationDTO;
import DTO.Metric.MetricHostDTO;
import DTO.Metric.MetricDTO;


import java.util.Date;

public interface MetricService {

    MetricAccommodationDTO getAccommodationMetric(String id, Date firstDate, Date lastDate) throws Exception;

    MetricHostDTO getHostMetric(Date firstDate, Date lastDate) throws Exception;
}
