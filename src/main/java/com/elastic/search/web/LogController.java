package com.elastic.search.web;

import com.elastic.search.model.LogDocument;
import com.elastic.search.service.LogSearchService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v2/logs")
public class LogController {

    private final LogSearchService logSearchService;

    public LogController(LogSearchService logSearchService) {
        this.logSearchService = logSearchService;
    }

    // Highly flexible endpoint: /api/v2/logs/search?keyword=timeout&level=WARN
    @GetMapping("/search")
    public ResponseEntity<List<LogDocument>> advancedSearch(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String level,
            @RequestParam(required = false) String service,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant to
    ) {
        List<LogDocument> results = logSearchService.searchLogsAdvanced(keyword, level, service, from, to);
        return ResponseEntity.ok(results);
    }

    // Analytics dashboard metrics: /api/v2/logs/metrics/by-service
    @GetMapping("/metrics/by-service")
    public ResponseEntity<Map<String, Long>> getServiceMetrics() {
        Map<String, Long> metrics = logSearchService.getLogCountByServiceAggregation();
        return ResponseEntity.ok(metrics);
    }
}