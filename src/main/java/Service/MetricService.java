package Service;

import DTO.Metric.MetricDTO;
import DTO.Metric.MetricHostDTO;

import java.util.Date;

public interface MetricService {

    MetricDTO getAccomodationMetric(String id, Date firstDate, Date lastDate) throws Exception;

    MetricHostDTO getHostMetric(Date firstDate, Date lastDate) throws Exception;
}
