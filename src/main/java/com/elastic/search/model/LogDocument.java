package com.elastic.search.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "app-logs")
public class LogDocument {

    @Id
    private String id;

    @Field(type = FieldType.Keyword)
    private String logLevel; // INFO, WARN, ERROR

    @Field(type = FieldType.Text)
    private String message;  // The actual log message

    @Field(type = FieldType.Keyword)
    private String serviceName; // e.g., "payment-service", "auth-service"

    @Field(type = FieldType.Date)
    private Instant timestamp;
}