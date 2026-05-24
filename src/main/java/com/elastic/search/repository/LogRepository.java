package com.elastic.search.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import com.elastic.search.model.LogDocument;

import java.util.List;

@Repository
public interface LogRepository extends ElasticsearchRepository<LogDocument, String> {
    
    // Find logs matching a certain level (e.g., fetch all "ERROR" logs)
    List<LogDocument> findByLogLevel(String logLevel);

    // Full-text search across log messages
    List<LogDocument> findByMessageContaining(String keyword);
}
