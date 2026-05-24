package com.elastic.search.service;

import com.elastic.search.model.LogDocument;
import com.elastic.search.repository.LogRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Service
@Slf4j // Lombok annotation for logging
public class RealTimeLogSimulator {

    private final LogRepository logRepository;
    private final Random random = new Random();

    private final List<String> levels = List.of("INFO", "WARN", "ERROR");
    private final List<String> services = List.of("auth-service", "payment-service", "inventory-service");
    private final List<String> messages = List.of(
            "User login successful",
            "Database connection timeout warning",
            "Payment gateway failed with status 500",
            "Cache eviction threshold reached",
            "Out of memory risk detected on heap cluster"
    );

    public RealTimeLogSimulator(LogRepository logRepository) {
        this.logRepository = logRepository;
    }

    // This background task runs automatically every 2000 milliseconds (2 seconds)
    @Scheduled(fixedRate = 2000)
    public void generateLogStream() {
        LogDocument mockLog = new LogDocument(
                UUID.randomUUID().toString(),
                levels.get(random.nextInt(levels.size())),
                messages.get(random.nextInt(messages.size())),
                services.get(random.nextInt(services.size())),
                Instant.now()
        );

        logRepository.save(mockLog);
        log.info(">>>> Streamed to Elasticsearch: [{}] from {}", mockLog.getLogLevel(), mockLog.getServiceName());
    }
}