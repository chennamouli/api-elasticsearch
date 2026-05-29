package com.elastic.search.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.MultiField;
import org.springframework.data.elasticsearch.annotations.InnerField; // <-- Make sure this is imported!

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "enterprise-logs-v1")
public class LogDocument {

    @Id
    private String id;

    @Field(type = FieldType.Keyword)
    private String logLevel; 

    @Field(type = FieldType.Keyword)
    private String serviceName;

    @MultiField(
        mainField = @Field(type = FieldType.Text, analyzer = "standard"),
        otherFields = { @InnerField(suffix = "keyword", type = FieldType.Keyword) }
    )
    private String message;

    @Field(type = FieldType.Date)
    private Instant timestamp;
    
    @Field(type = FieldType.Integer)
    private int httpStatusCode;
}