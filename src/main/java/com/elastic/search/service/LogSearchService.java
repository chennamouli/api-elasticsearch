package com.elastic.search.service;

import com.elastic.search.model.LogDocument;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations; 
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import co.elastic.clients.elasticsearch._types.aggregations.Aggregate;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.aggregations.StringTermsBucket;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class LogSearchService {

    private final ElasticsearchOperations elasticsearchOperations;

    public LogSearchService(ElasticsearchOperations elasticsearchOperations) {
        this.elasticsearchOperations = elasticsearchOperations;
    }

    public List<LogDocument> searchLogsAdvanced(String keyword, String level, String service, Instant from, Instant to) {
        
        BoolQuery.Builder boolQueryBuilder = new BoolQuery.Builder();

        // 1. Full-Text Search
        if (StringUtils.hasText(keyword)) {
            boolQueryBuilder.must(m -> m
                .match(match -> match
                    .field("message")
                    .query(keyword)
                    .fuzziness("AUTO")
                )
            );
        }

        // 2. Exact Filters
        if (StringUtils.hasText(level)) {
            boolQueryBuilder.filter(f -> f.term(t -> t.field("logLevel").value(level.toUpperCase())));
        }

        if (StringUtils.hasText(service)) {
            boolQueryBuilder.filter(f -> f.term(t -> t.field("serviceName").value(service)));
        }

        // 3. Date Range Filter Clause
        if (from != null || to != null) {
            boolQueryBuilder.filter(f -> f
                .range(r -> r
                    .term(t -> {
                        t.field("timestamp");
                        if (from != null) {
                            t.gte(from.toString());
                        }
                        if (to != null) {
                            t.lte(to.toString());
                        }
                        return t;
                    })
                )
            );
        }

        Query searchQuery = new NativeQueryBuilder()
                .withQuery(boolQueryBuilder.build()._toQuery())
                .withMaxResults(100)
                .build();

        SearchHits<LogDocument> searchHits = elasticsearchOperations.search(searchQuery, LogDocument.class);

        return searchHits.stream()
                .map(hit -> hit.getContent())
                .collect(Collectors.toList());
    }

    public Map<String, Long> getLogCountByServiceAggregation() {
    String aggName = "services_distribution";

    // 1. Build the aggregation query normally
    Query aggregationQuery = new NativeQueryBuilder()
            .withAggregation(aggName, Aggregation.of(a -> a
                    .terms(t -> t.field("serviceName"))
            ))
            .withMaxResults(0) 
            .build();

    // 2. Execute the search
    SearchHits<LogDocument> searchHits = elasticsearchOperations.search(aggregationQuery, LogDocument.class);
    
    if (searchHits.getAggregations() == null) {
        return Map.of();
    }

    Map<String, Long> resultMap = new HashMap<>();

    try {
        // 3. Cast the generic aggregations container to Spring's concrete plural implementation class
        org.springframework.data.elasticsearch.client.elc.ElasticsearchAggregations container = 
            (org.springframework.data.elasticsearch.client.elc.ElasticsearchAggregations) searchHits.getAggregations();
        
        // 4. FIX: Access the list of aggregations directly
        java.util.List<org.springframework.data.elasticsearch.client.elc.ElasticsearchAggregation> aggList = 
            container.aggregations();

        // 5. Look for the aggregation matching our target name inside the list
        for (org.springframework.data.elasticsearch.client.elc.ElasticsearchAggregation esAgg : aggList) {
            if (aggName.equals(esAgg.name())) {
                
                // Extract the inner core native client Aggregate block
                Aggregate aggregate = esAgg.aggregation().aggregate();
                
                // Parse out the string terms buckets
                if (aggregate != null && aggregate.isSterms()) {
                    java.util.List<StringTermsBucket> buckets = aggregate.sterms().buckets().array();
                    for (StringTermsBucket bucket : buckets) {
                        String key = bucket.key().stringValue();
                        long count = bucket.docCount();
                        resultMap.put(key, count);
                    }
                }
                break; // Found our match, exit the loop early
            }
        }
    } catch (Exception e) {
        System.err.println("Metrics extraction loop exception: " + e.getMessage());
    }

    return resultMap;
}


}