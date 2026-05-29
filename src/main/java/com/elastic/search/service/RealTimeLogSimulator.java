package com.elastic.search.service;

import com.elastic.search.model.LogDocument;
import com.elastic.search.repository.LogRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Service
public class RealTimeLogSimulator {

    private final LogRepository logRepository;
    private final Random random = new Random();

    private final List<String> levels = List.of("INFO", "WARN", "ERROR");
    private final List<String> services = List.of("payment-service", "auth-api", "gateway-proxy");
    private final List<Integer> statuses = List.of(200, 201, 400, 401, 404, 500);

    public RealTimeLogSimulator(LogRepository logRepository) {
        this.logRepository = logRepository;
    }

    @Scheduled(fixedRate = 1500)
    public void generateData() {
        LogDocument entity = LogDocument.builder()
                .id(UUID.randomUUID().toString())
                .logLevel(levels.get(random.nextInt(levels.size())))
                .serviceName(services.get(random.nextInt(services.size())))
                .message("System performance trace check status notification logs alert " + random.nextInt(100))
                .httpStatusCode(statuses.get(random.nextInt(statuses.size())))
                .timestamp(Instant.now())
                .build();

        logRepository.save(entity);
    }
}