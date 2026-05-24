package com.elastic.search.web;

import com.elastic.search.model.LogDocument;
import com.elastic.search.repository.LogRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/logs")
public class LogController {

    private final LogRepository logRepository;

    public LogController(LogRepository logRepository) {
        this.logRepository = logRepository;
    }

    // Get all logs currently in Elasticsearch
    @GetMapping
    public Iterable<LogDocument> getAllLogs() {
        return logRepository.findAll();
    }

    // Filter logs by level (e.g., /api/logs/level/ERROR)
    @GetMapping("/level/{level}")
    public List<LogDocument> getLogsByLevel(@PathVariable String level) {
        return logRepository.findByLogLevel(level.toUpperCase());
    }

    // Full text search inside the log message (e.g., /api/logs/search?q=failed)
    @GetMapping("/search")
    public List<LogDocument> searchLogs(@RequestParam String q) {
        return logRepository.findByMessageContaining(q);
    }
}