package com.elastic.search.repository;

import com.elastic.search.model.Movie;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MovieRepository extends ElasticsearchRepository<Movie, String> {
    
    // Spring Data writes the Elasticsearch "Match" text query for you!
    List<Movie> findByDescriptionContaining(String descriptionWord);
    
    // You can also search by exact matches for structured data like year
    List<Movie> findByReleaseYear(int year);
}