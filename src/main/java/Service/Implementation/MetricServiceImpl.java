package Service.Implementation;

import DTO.Metric.MetricHostDTO;
import DTO.MetricAccommodationDTO;
import Service.MetricService;

import java.util.Date;

public class MetricServiceImpl implements MetricService {
    @Override
    public MetricAccommodationDTO getAccommodationMetric(String id, Date firstDate, Date lastDate) throws Exception {
        return null;
    }

    @Override
    public MetricHostDTO getHostMetric(Date firstDate, Date lastDate) throws Exception {
        return null;
    }
}
