package eu.kielczewski.example.service.metric;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.metrics.Metric;
import org.springframework.boot.actuate.metrics.repository.MetricRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import static net.logstash.logback.marker.Markers.append;

@Service
class MetricExporterService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MetricExporterService.class);
    private final MetricRepository repository;

    @Autowired
    public MetricExporterService(MetricRepository repository) {
        this.repository = repository;
    }

    @Scheduled(initialDelay = 60000, fixedDelay = 60000)
    void exportMetrics() {
        LOGGER.debug("Reporting metrics");
        repository.findAll().forEach(this::log);
    }

    private void log(Metric<?> m) {
        LOGGER.info(append("metric", m), "Reporting metric {}={}", m.getName(), m.getValue());
        repository.reset(m.getName());
    }

}