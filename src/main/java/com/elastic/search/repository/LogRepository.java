package com.elastic.search.repository;

import com.elastic.search.model.LogDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogRepository extends ElasticsearchRepository<LogDocument, String> {
    // Basic CRUD operations inherited automatically
}